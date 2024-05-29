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
fun solve(rpn: ReversePolishSequence, state: State): Result<BigDecimal> { //TODO use tryCatch?
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
            return Result.failure(InputMismatchException("unexpected token"))
        }
        val (tokenConsume, callArguments, function) = when (val it = solveStack[noneNumIdx]) {
            is Token.Function -> Triple(it.numArgs, it.numArgs, it.function)
            is Token.Operator -> Triple(it.numArgs, it.numArgs, it.function)
            is Token.Assignment -> Triple(2, 1) { args ->
                val dec = args.first()
                state.identifiers[solveStack[noneNumIdx - 2].string] = dec
                dec
            }

            else -> return Result.failure(InputMismatchException("unexpected token"))
        }
        if (noneNumIdx < callArguments) {
            return Result.failure(InputMismatchException("not enough tokens"))
        }
        val result = function.invoke(solveStack.subList(noneNumIdx - callArguments, noneNumIdx).map {
            when (it) {
                is Token.Number -> it.value
                is Token.Value -> {
                    tryCatch { state.identifiers.getValue(it.string) }.getOrElse { return Result.failure(it) }
                }

                else -> return Result.failure(InputMismatchException("unexpected token"))
            }
        })
        solveStack[noneNumIdx - tokenConsume] = Token.Number(result)
        for (i in 1..(tokenConsume)) {
            solveStack.removeAt(noneNumIdx - tokenConsume + 1)
        }
    }
    return when (val it = solveStack.firstOrNull()) {
        is Token.Number -> Result.success(it.value)
        is Token.Value -> tryCatch { state.identifiers.getValue(it.string) }
        null -> Result.failure(InputMismatchException("not enough tokens"))
        else -> Result.failure(InputMismatchException("unexpected token"))
    }
}
