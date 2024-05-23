package moe.sgs.kt.shuntingyard

class InfixSequence(val str: String, val arithmeticContext: ArithmeticContext) : Sequence<Token> {
    override fun iterator() = object : Iterator<Token> {

        override fun hasNext() = false
        override fun next(): Token {
            TODO()
        }
    }
}

fun String.toInfixSequence(arithmeticContext: ArithmeticContext = DefaultArithmeticContext()): InfixSequence =
    InfixSequence(this, arithmeticContext)
