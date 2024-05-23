package moe.sgs.kt.shuntingyard

const val INPUT_PREFIX = "|> "
const val OUTPUT_PREFIX = "|: "

class Token

class State {
    fun process(input: String): Result<Token> {
        TODO()
    }
}

fun main() {
    val state = State()
    try {
        while (true) {
            print(INPUT_PREFIX)
            val input = readln()
            val result = state.process(input).getOrThrow()
            print(OUTPUT_PREFIX)
            print(result)
        }
    } catch (e: Exception) {
        print("\n\nExit: ")
        println(e.message)
    }
}
