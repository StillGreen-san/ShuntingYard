package moe.sgs.kt.shuntingyard

/**
 * Represents a readable sequence of [Token] values
 *
 * @property infixSequence of [Token] in infix order to reorder
 */
class ReversePolishSequence(val infixSequence: Sequence<Token>) : Sequence<Token> {
    override fun iterator() = iterator {
        val operatorStack = ArrayList<Token>()
        for (token in infixSequence) {
            when (token) {
                is Token.Number -> yield(token)
                is Token.Value -> yield(token)
                is Token.Function -> operatorStack.add(token)
                is Token.Assignment -> handleOperator(operatorStack, token)
                is Token.Operator -> handleOperator(operatorStack, token)
                is Token.Komma -> handleKomma(operatorStack)
                is Token.OpenParen -> operatorStack.add(token)
                is Token.CloseParen -> handleCloseParen(operatorStack)
                is Token.None -> throw IllegalArgumentException("invalid token 'none'")
            }
        }
        do {
            val topOperator = operatorStack.removeLastOrNull()
            require(topOperator !is Token.OpenParen) { "mismatched parentheses" }
            topOperator?.let { yield(it) }
        } while (topOperator != null)
    }

    private suspend fun SequenceScope<Token>.handleKomma(operatorStack: ArrayList<Token>) {
        while (when (operatorStack.lastOrNull()) {
                is Token.OpenParen -> false
                null -> false
                else -> true
            }
        ) {
            yield(operatorStack.removeLast())
        }
    }

    private suspend fun SequenceScope<Token>.handleCloseParen(operatorStack: ArrayList<Token>) {
        while (when (operatorStack.lastOrNull()) {
                is Token.OpenParen -> false
                null -> throw IllegalArgumentException("mismatched parentheses")
                else -> true
            }
        ) {
            yield(operatorStack.removeLast())
        }
        require(operatorStack.lastOrNull() is Token.OpenParen)
        operatorStack.removeLast()
        if (operatorStack.lastOrNull() is Token.Function) {
            yield(operatorStack.removeLast())
        }
    }

    private suspend fun SequenceScope<Token>.handleOperator(
        operatorStack: ArrayList<Token>,
        token: Token.BaseOperator
    ) {
        while (when (val topOperator = operatorStack.lastOrNull()) {
                is Token.Operator -> {
                    val higherPrecedent = topOperator.precedent > token.precedent
                    val equalPrecedent = topOperator.precedent == token.precedent
                    val leftAssociative = token.associativity == Associativity.LeftAssociative
                    higherPrecedent || (equalPrecedent && leftAssociative)
                }

                else -> false
            }
        ) {
            yield(operatorStack.removeLast())
        }
        operatorStack.add(token)
    }
}

/**
 * Creates a [Sequence] instance that wraps the original [Sequence] returning its [Token]s in reverse polish
 * order when being iterated
 *
 * @return [ReversePolishSequence]
 */
fun Sequence<Token>.toReversePolishSequence(): ReversePolishSequence = ReversePolishSequence(this)
