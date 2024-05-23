package moe.sgs.kt.shuntingyard

class ReversePolishSequence(private val seq: Sequence<Token>) : Sequence<Token> {
    override fun iterator() = object : Iterator<Token> {

        override fun hasNext() = false
        override fun next(): Token {
            TODO()
        }
    }
}

fun Sequence<Token>.toReversePolishSequence(): ReversePolishSequence = ReversePolishSequence(this)
