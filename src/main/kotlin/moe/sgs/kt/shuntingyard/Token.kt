// SPDX-License-Identifier: MPL-2.0

package moe.sgs.kt.shuntingyard

import java.math.BigDecimal

/**
 * A callback function for [Token] to process a variable number of BigDecimal
 */
typealias TokenFunction = (Iterable<BigDecimal>) -> BigDecimal

enum class Associativity {
    LeftAssociative,
    RightAssociative,
}

sealed class Token {
    abstract val string: String

    data class Number(val value: BigDecimal, override val string: String = value.toString()) : Token() {
        constructor(int: Int) : this(BigDecimal(int))
    }

    data class Value(override val string: String) : Token()
    data class Function(
        override val string: String,
        val numArgs: Int,
        val function: TokenFunction,
    ) : Token() {
        constructor(tokenFunctionData: TokenFunctionData)
                : this(tokenFunctionData.name, tokenFunctionData.numArgs, tokenFunctionData.function)
    }

    sealed class BaseOperator : Token() {
        abstract val precedent: Int
        abstract val associativity: Associativity

    }

    data class Operator(
        override val string: String,
        val numArgs: Int,
        override val precedent: Int,
        override val associativity: Associativity,
        val function: TokenFunction,
    ) : BaseOperator() {
        constructor(tokenOperatorData: TokenOperatorData) : this(
            tokenOperatorData.name,
            tokenOperatorData.numArgs,
            tokenOperatorData.precedent,
            tokenOperatorData.associativity,
            tokenOperatorData.function,
        )
    }

    data class Assignment(
        override val string: String = "=",
        override val precedent: Int = 1,
        override val associativity: Associativity = Associativity.RightAssociative
    ) : BaseOperator()


    data class Komma(override val string: String = ",") : Token()
    data class OpenParen(override val string: String = "(") : Token()
    data class CloseParen(override val string: String = ")") : Token()
    data class None(override val string: String = "") : Token()
}
