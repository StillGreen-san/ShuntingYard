package moe.sgs.kt.shuntingyard

import java.math.BigDecimal

typealias TokenFunction = (Iterable<BigDecimal>) -> BigDecimal

enum class Associativity {
    LeftAssociative,
    RightAssociative,
}

sealed class Token {
    data class Number(val value: BigDecimal, val string: String = value.toString()) : Token() {
        constructor(int: Int) : this(BigDecimal(int))
    }

    sealed class Identifier : Token() {
        data class Value(val string: String) : Identifier()
        data class Function(
            val numArgs: Int,
            val function: TokenFunction,
            val string: String
        ) : Identifier()
    }

    data class Operator(
        val numArgs: Int,
        val function: TokenFunction,
        val precedent: Int,
        val associativity: Associativity,
        val string: String
    ) : Token()

    data class Assignment(val string: String = "=") : Token()

    data class Komma(val string: String = ",") : Token()
    data class OpenParen(val string: String = "(") : Token()
    data class CloseParen(val string: String = ")") : Token()
    data class None(val string: String = "") : Token()
}
