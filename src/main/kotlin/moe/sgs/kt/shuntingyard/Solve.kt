// SPDX-License-Identifier: MPL-2.0

package moe.sgs.kt.shuntingyard

import java.math.BigDecimal
import java.util.*
import kotlin.math.max

/**
 * Solve an arithmetic expression in reverse polish notation
 *
 * @param rpn [ReversePolishSequence]
 * @param state [State]
 * @return [Result]
 */
@Suppress("t")
fun solve(rpn: ReversePolishSequence, state: State): Result<BigDecimal> = tryCatch {
    val couldNotFindOperation =
        "did not find expected token of ${nameNoPackage<Token.Function>()}, ${nameNoPackage<Token.Operator>()} or ${nameNoPackage<Token.Assignment>()}"
    val expectedNumOrValFound = { it: Any ->
        "expected ${nameNoPackage<Token.Number>()} or ${nameNoPackage<Token.Value>()} found ${nameNoPackage(it)}"
    }

    val solveStack = rpn.toMutableList()
    while (solveStack.size > 1) {
        val noneNumIdx = solveStack.indexOfFirst {
            when (it) {
                is Token.Function -> true
                is Token.Operator -> true
                is Token.Assignment -> true
                else -> false
            }
        }
        if (noneNumIdx == -1) {
            throw InputMismatchException(couldNotFindOperation)
        }
        val (tokenConsume, callArguments, function) = when (val token = solveStack[noneNumIdx]) {
            is Token.Function -> Triple(token.numArgs, token.numArgs, token.function)
            is Token.Operator -> Triple(token.numArgs, token.numArgs, token.function)
            is Token.Assignment -> Triple(2, 1) { args ->
                val dec = args.first()
                when (val maybeValue = solveStack[noneNumIdx - 2]) {
                    is Token.Value -> state.identifiers[maybeValue.string] = dec
                    else -> throw InputMismatchException(
                        "expected ${nameNoPackage<Token.Value>()} found ${nameNoPackage(maybeValue)}"
                    )
                }

                dec
            }

            else -> throw InputMismatchException(couldNotFindOperation)
        }
        if (noneNumIdx < callArguments || noneNumIdx < tokenConsume) {
            throw InputMismatchException(
                "expected ${max(callArguments, tokenConsume)} more tokens only found ${noneNumIdx - 1}"
            )
        }
        val result = function.invoke(solveStack.subList(noneNumIdx - callArguments, noneNumIdx).map {
            when (it) {
                is Token.Number -> it.value
                is Token.Value -> state.identifiers.getValue(it.string)
                else -> throw InputMismatchException(expectedNumOrValFound(it))
            }
        })
        solveStack[noneNumIdx - tokenConsume] = Token.Number(result)
        for (i in 1..(tokenConsume)) {
            solveStack.removeAt(noneNumIdx - tokenConsume + 1)
        }
    }
    when (val it = solveStack.firstOrNull()) {
        is Token.Number -> it.value
        is Token.Value -> state.identifiers.getValue(it.string)
        null -> throw InputMismatchException("no tokens left after evaluation")
        else -> throw InputMismatchException(expectedNumOrValFound(it))
    }
}.finalize(state)
