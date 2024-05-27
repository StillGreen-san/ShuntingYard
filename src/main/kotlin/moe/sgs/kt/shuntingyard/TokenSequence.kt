package moe.sgs.kt.shuntingyard

import java.io.EOFException
import java.math.BigDecimal
import java.util.*

/**
 *  Represents a readable sequence of [Token] values.
 *
 * @property string of the arithmetic expression to tokenize
 * @property arithmeticContext for mapping special tokens
 */
class TokenSequence(val string: String, val arithmeticContext: ArithmeticContext) : Sequence<Token> {
    override fun iterator() = object : Iterator<Token> {
        var remaining = string
        var nextToken = tryNext()
        var prevToken: Token = Token.None()

        override fun hasNext() = nextToken.isSuccess
        override fun next(): Token {
            val currentToken = nextToken.getOrThrow()
            nextToken = tryNext()
            return currentToken
        }

        private fun tryNext(): Result<Token> {
            remaining = remaining.trimStart()
            if (remaining.isEmpty()) {
                return Result.failure(EOFException("no more tokens left to parse"))
            }
            return runCatching { internalNext() }.onSuccess {
                remaining = remaining.substring(it.string.length)
                prevToken = it
            }
        }

        private fun internalNext(): Token {
            if (remaining.first().isDigit() || (remaining.first() == '-' && remaining.second().isDigit()
                        && (prevToken !is Token.Number && prevToken !is Token.CloseParen))
            ) {
                val endOfNumber = remaining.drop(1).indexOfFirstOrLength { c -> !(c.isDigit() || c == '.') } + 1
                val number = BigDecimal(remaining.toCharArray(0, endOfNumber))
                return Token.Number(number)
            }
            if (remaining.first().isLetter()) {
                val name = remaining.substring(0, remaining.indexOfFirstOrLength { c -> !c.isLetter() })
                return when (val functionData = arithmeticContext.functions[name]) {
                    null -> Token.Value(name)
                    else -> Token.Function(functionData)
                }
            }
            return when (remaining.first()) {
                ',' -> Token.Komma()
                '(' -> Token.OpenParen()
                ')' -> Token.CloseParen()
                '=' -> Token.Assignment()
                else -> {
                    val name = remaining.substring(0, 1)
                    val operatorData = arithmeticContext.operators.getOrElse(name) {
                        throw InputMismatchException("unknown operator '{name}'")
                    }
                    Token.Operator(operatorData)
                }
            }
        }
    }
}

/**
 * Creates a [Sequence] instance that wraps the original [String] returning its [Token]s when being iterated.
 *
 * @param arithmeticContext defaulted to DefaultArithmeticContext
 * @return [TokenSequence]
 */
fun String.asTokenSequence(arithmeticContext: ArithmeticContext = DefaultArithmeticContext): TokenSequence =
    TokenSequence(this, arithmeticContext)
