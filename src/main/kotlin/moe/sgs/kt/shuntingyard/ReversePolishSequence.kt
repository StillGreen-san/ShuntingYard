package moe.sgs.kt.shuntingyard

/**
 * Represents a readable sequence of [Token] values
 *
 * @property sequence of [Token] to reorder
 */
class ReversePolishSequence(val sequence: Sequence<Token>) : Sequence<Token> {
    override fun iterator() = object : Iterator<Token> {

        override fun hasNext() = false
        override fun next(): Token {
            TODO()
        }
    }
}

/**
 * Creates a [Sequence] instance that wraps the original [Sequence] returning its [Token]s in reverse polish
 * order when being iterated
 *
 * @return [ReversePolishSequence]
 */
fun Sequence<Token>.toReversePolishSequence(): ReversePolishSequence = ReversePolishSequence(this)
