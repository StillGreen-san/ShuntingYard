// SPDX-License-Identifier: MPL-2.0

// SPDX-License-Identifier: MPL-2.0

package moe.sgs.kt.shuntingyard

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
        var remaining = string.trim()
        var prevToken: Token = Token.None()

        override fun hasNext() = remaining.isNotEmpty()
        override fun next(): Token {
            if (!hasNext()) {
                throw NoSuchElementException("no more tokens left to parse")
            }
            val result = internalNext()
            remaining = remaining.substring(result.string.length).trimStart()
            prevToken = result
            return result
        }

        private fun internalNext(): Token {
            if (remaining.first().isDigit() || (remaining.first() == '-' && remaining.secondOrNull()?.isDigit() == true
                        && (prevToken !is Token.Number && prevToken !is Token.CloseParen))
            ) {
                val endOfNumber = remaining.drop(1).indexOfFirstOrLength { c -> !(c.isDigit() || c == '.') } + 1
                val number = BigDecimal(remaining.toCharArray(0, endOfNumber))
                return Token.Number(number)
            }
            if (remaining.first().isLetter()) {
                val name = remaining.substring(0, remaining.indexOfFirstOrLength { c -> !c.isLetter() })
                when (val functionData = arithmeticContext.functions[name]) {
                    is TokenFunctionData -> return Token.Function(functionData)
                }
                return when (val decimal = arithmeticContext.identifiers[name]) {
                    null -> Token.Value(name)
                    else -> Token.Number(decimal, name)
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
                        throw InputMismatchException("unknown operator '$name'")
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
 * @param arithmeticContext defaulted to [DefaultArithmeticContext]
 * @return [TokenSequence]
 */
fun String.asTokenSequence(arithmeticContext: ArithmeticContext = DefaultArithmeticContext): TokenSequence =
    TokenSequence(this, arithmeticContext)
