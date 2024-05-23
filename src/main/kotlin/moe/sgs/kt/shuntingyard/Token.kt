package moe.sgs.kt.shuntingyard

import java.math.BigDecimal

typealias TokenFunction = (Iterable<BigDecimal>) -> BigDecimal

enum class Associativity {
    LeftAssociative,
    RightAssociative,
}

sealed class Token(val string: String) {
    class Number(val value: BigDecimal) : Token(value.toString())

    sealed class Identifier(string: String) : Token(string) {
        class Value(string: String) : Identifier(string)
        class Function(
            val numArgs: Int,
            val function: TokenFunction,
            string: String
        ) : Identifier(string)
    }

    class Operator(
        val numArgs: Int,
        val function: TokenFunction,
        val precedent: Int,
        val associativity: Associativity,
        string: String
    ) : Token(string)

    data object Assignment : Token("=")

    data object Komma : Token(",")
    data object OpenParen : Token("(")
    data object CloseParen : Token(")")
    data object None : Token("")
}
