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

    val rpnIt = rpn.iterator()
    val solveStack = mutableListOf<Token>()
    if (rpnIt.hasNext()) {
        solveStack.add(rpnIt.next())
    }
    while (rpnIt.hasNext() || solveStack.size > 1) {
        while (rpnIt.hasNext()) {
            solveStack.add(rpnIt.next())
            when (solveStack.last()) {
                is Token.Function -> break
                is Token.Operator -> break
                is Token.Assignment -> break
                else -> continue
            }
        }
        val operationIdx = solveStack.lastIndex
        val (tokenConsume, callArguments, function) = when (val token = solveStack.elementAtOrNull(operationIdx)) {
            is Token.Function -> Triple(token.numArgs, token.numArgs, token.function)
            is Token.Operator -> Triple(token.numArgs, token.numArgs, token.function)
            is Token.Assignment -> Triple(2, 1) { args ->
                val dec = args.first()
                when (val maybeValue = solveStack[operationIdx - 2]) {
                    is Token.Value -> state.identifiers[maybeValue.string] = dec
                    else -> throw InputMismatchException(
                        "expected ${nameNoPackage<Token.Value>()} found ${nameNoPackage(maybeValue)}"
                    )
                }

                dec
            }

            else -> throw InputMismatchException(couldNotFindOperation)
        }
        if (operationIdx < callArguments || operationIdx < tokenConsume) {
            throw InputMismatchException(
                "expected ${max(callArguments, tokenConsume)} more tokens only found ${operationIdx - 1}"
            )
        }
        val result = function.invoke(solveStack.subList(operationIdx - callArguments, operationIdx).map {
            when (it) {
                is Token.Number -> it.value
                is Token.Value -> state.identifiers.getValue(it.string)
                else -> throw InputMismatchException(expectedNumOrValFound(it))
            }
        })
        solveStack[operationIdx - tokenConsume] = Token.Number(result)
        for (i in 1..(tokenConsume)) {
            solveStack.removeAt(operationIdx - tokenConsume + 1)
        }
    }
    when (val it = solveStack.firstOrNull()) {
        is Token.Number -> it.value
        is Token.Value -> state.identifiers.getValue(it.string)
        null -> throw InputMismatchException("no tokens left after evaluation")
        else -> throw InputMismatchException(expectedNumOrValFound(it))
    }
}.finalize(state)
