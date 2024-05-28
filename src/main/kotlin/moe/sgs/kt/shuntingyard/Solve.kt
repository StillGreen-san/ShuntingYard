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
fun solve(rpn: ReversePolishSequence, state: State): Result<BigDecimal> {
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
        if(noneNumIdx == -1) {
            return Result.failure(InputMismatchException("unexpected token"))
        }
        val (args, func) = when (val it = solveStack[noneNumIdx]) {
            is Token.Function -> Pair(it.numArgs, it.function)
            is Token.Operator -> Pair(it.numArgs, it.function)
            is Token.Assignment -> Pair(2) { args ->
                val dec = args.last()
                state.identifiers[it.string] = dec
                dec
            }

            else -> return Result.failure(InputMismatchException("unexpected token"))
        }
        val result = func.invoke(solveStack.subList(noneNumIdx - args, noneNumIdx).map {
            when(it) {
                is Token.Number -> it.value
                is Token.Value -> state.identifiers.getValue(it.string)
                else -> return Result.failure(InputMismatchException("unexpected token"))
            }
        })
        solveStack[noneNumIdx - args] = Token.Number(result)
        for (i in 1..(args)) {
            solveStack.removeAt(noneNumIdx - args + 1)
        }
    }
    return Result.success((solveStack.first() as Token.Number).value)
}
