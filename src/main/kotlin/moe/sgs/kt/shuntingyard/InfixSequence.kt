package moe.sgs.kt.shuntingyard

class InfixSequence(private val str: String) : Sequence<Token> {
    override fun iterator() = object : Iterator<Token> {

        override fun hasNext() = false
        override fun next(): Token {
            TODO()
        }
    }
}

fun String.toInfixSequence(): InfixSequence = InfixSequence(this)
