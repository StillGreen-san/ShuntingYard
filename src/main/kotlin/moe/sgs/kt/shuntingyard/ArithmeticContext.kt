// SPDX-License-Identifier: MPL-2.0

package moe.sgs.kt.shuntingyard

import java.math.BigDecimal

data class TokenFunctionData(val name: String, val numArgs: Int, val function: TokenFunction) {
    companion object {
        fun make(name: String, func: (BigDecimal, BigDecimal) -> BigDecimal): TokenFunctionData {
            return TokenFunctionData(name, 2) { it: Iterable<BigDecimal> ->
                val (lhs, rhs) = it.toList()
                func(lhs, rhs)
            }
        }

        fun make(name: String, func: (BigDecimal) -> BigDecimal): TokenFunctionData {
            return TokenFunctionData(name, 1) { it: Iterable<BigDecimal> ->
                val lhs = it.iterator().next()
                func(lhs)
            }
        }
    }
}

data class TokenOperatorData(
    val name: String,
    val numArgs: Int,
    val precedent: Int,
    val associativity: Associativity,
    val function: TokenFunction
) {
    companion object {
        fun make(
            name: String,
            precedent: Int,
            associativity: Associativity,
            func: (BigDecimal, BigDecimal) -> BigDecimal
        ): TokenOperatorData {
            return TokenOperatorData(name, 2, precedent, associativity)
            { it: Iterable<BigDecimal> ->
                val (lhs, rhs) = it.toList()
                func(lhs, rhs)
            }
        }
    }
}

/**
 * Provides mappings between String and Token*Data
 *
 */
interface ArithmeticContext {
    val identifiers: Map<String, BigDecimal>
    val functions: Map<String, TokenFunctionData>
    val operators: Map<String, TokenOperatorData>
}
