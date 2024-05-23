package moe.sgs.kt.shuntingyard

import java.math.BigDecimal

data class TokenFunctionData(val numArgs: Int, val function: TokenFunction) {
    companion object {
        fun make(func: (BigDecimal, BigDecimal) -> BigDecimal): TokenFunctionData {
            return TokenFunctionData(2) { it: Iterable<BigDecimal> ->
                val (lhs, rhs) = it.toList()
                func(lhs, rhs)
            }
        }

        fun make(func: (BigDecimal) -> BigDecimal): TokenFunctionData {
            return TokenFunctionData(1) { it: Iterable<BigDecimal> ->
                val lhs = it.iterator().next()
                func(lhs)
            }
        }
    }
}

data class TokenOperatorData(
    val numArgs: Int,
    val precedent: Int,
    val associativity: Associativity,
    val function: TokenFunction
) {
    companion object {
        fun make(
            precedent: Int,
            associativity: Associativity,
            func: (BigDecimal, BigDecimal) -> BigDecimal
        ): TokenOperatorData {
            return TokenOperatorData(2, precedent, associativity)
            { it: Iterable<BigDecimal> ->
                val (lhs, rhs) = it.toList()
                func(lhs, rhs)
            }
        }
    }
}

interface ArithmeticContext {
    val functions: Map<String, TokenFunctionData>
    val operators: Map<String, TokenOperatorData>
}
