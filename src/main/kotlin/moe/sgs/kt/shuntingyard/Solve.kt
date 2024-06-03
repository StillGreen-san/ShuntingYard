package moe.sgs.kt.shuntingyard

import java.math.BigDecimal
import java.util.*

/**
 * Solve an arithmetic expression in reverse polish notation
 *
 * @param rpn [ReversePolishSequence]
 * @param state [State]
 * @return [Result]
 */
@Suppress("t")
fun solve(rpn: ReversePolishSequence, state: State): Result<BigDecimal> = tryCatch {
    val solveStack = rpn.toMutableList() //TODO lazy iteration?
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
            throw InputMismatchException("unexpected token")
        }
        val (tokenConsume, callArguments, function) = when (val it = solveStack[noneNumIdx]) {
            is Token.Function -> Triple(it.numArgs, it.numArgs, it.function)
            is Token.Operator -> Triple(it.numArgs, it.numArgs, it.function)
            is Token.Assignment -> Triple(2, 1) { args ->
                val dec = args.first()
                when (solveStack[noneNumIdx - 2]) {
                    is Token.Value -> state.identifiers[it.string] = dec
                    else -> throw InputMismatchException(
                        "expected ${Token.Value::class.java.nameNoPackage()} found ${it.javaClass.nameNoPackage()}"
                    )
                }

                dec
            }

            else -> throw InputMismatchException("unexpected token")
        }
        if (noneNumIdx < callArguments || noneNumIdx < tokenConsume) {
            throw InputMismatchException("not enough tokens")
        }
        val result = function.invoke(solveStack.subList(noneNumIdx - callArguments, noneNumIdx).map {
            when (it) {
                is Token.Number -> it.value
                is Token.Value -> state.identifiers.getValue(it.string)
                else -> throw InputMismatchException("unexpected token") //TODO better error descriptions
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
        null -> throw InputMismatchException("not enough tokens")
        else -> throw InputMismatchException("unexpected token")
    }
}.finalize(state)
