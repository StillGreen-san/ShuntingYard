package moe.sgs.kt.shuntingyard

const val INPUT_PREFIX = "|> "
const val OUTPUT_PREFIX = "|: "

fun main() {
    val state = State()
    try {
        while (true) {
            print(INPUT_PREFIX)
            val input = readln()
            val tokens = input.toInfixSequence()
            val rpn = tokens.toReversePolishSequence()
            val result = solve(rpn, state).getOrThrow()
            print(OUTPUT_PREFIX)
            println(result)
        }
    } catch (e: Exception) {
        print("\n\nExit: ")
        println(e.message)
    }
}
