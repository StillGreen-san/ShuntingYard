package moe.sgs.kt.shuntingyard

import java.math.BigDecimal

typealias TokenFunction = (Iterable<BigDecimal>) -> BigDecimal

enum class Associativity {
    LeftAssociative,
    RightAssociative,
}

sealed class Token {
    open val string: String
        get() = when (this) {
            is Assignment -> (this as Assignment).string
            is CloseParen -> (this as CloseParen).string
            is Function -> (this as Function).string
            is Value -> (this as Value).string
            is Komma -> (this as Komma).string
            is None -> (this as None).string
            is Number -> (this as Number).string
            is OpenParen -> (this as OpenParen).string
            is Operator -> (this as Operator).string
        }

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

    data class Operator(
        override val string: String,
        val numArgs: Int,
        val precedent: Int,
        val associativity: Associativity,
        val function: TokenFunction,
    ) : Token() {
        constructor(tokenOperatorData: TokenOperatorData) : this(
            tokenOperatorData.name,
            tokenOperatorData.numArgs,
            tokenOperatorData.precedent,
            tokenOperatorData.associativity,
            tokenOperatorData.function,
        )
    }

    data class Assignment(override val string: String = "=") : Token()

    data class Komma(override val string: String = ",") : Token()
    data class OpenParen(override val string: String = "(") : Token()
    data class CloseParen(override val string: String = ")") : Token()
    data class None(override val string: String = "") : Token()
}
