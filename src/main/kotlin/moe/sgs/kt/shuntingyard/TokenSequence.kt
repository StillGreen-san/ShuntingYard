package moe.sgs.kt.shuntingyard

/**
 *  Represents a readable sequence of [Token] values.
 *
 * @property string of the arithmetic expression to tokenize
 * @property arithmeticContext for mapping special tokens
 */
class TokenSequence(val string: String, val arithmeticContext: ArithmeticContext) : Sequence<Token> {
    override fun iterator() = object : Iterator<Token> {

        override fun hasNext() = false
        override fun next(): Token {
            TODO()
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
