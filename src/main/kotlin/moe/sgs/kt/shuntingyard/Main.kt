package moe.sgs.kt.shuntingyard

const val INPUT_PREFIX = "|> "
const val OUTPUT_PREFIX = "|: "

enum class KeywordAction {
    Exit,
    Reset,
}

val ACTIONS = mapOf(
    "quit" to KeywordAction.Exit,
    "exit" to KeywordAction.Exit,
    ":q" to KeywordAction.Exit,
    "reset" to KeywordAction.Reset,
    "clear" to KeywordAction.Reset,
)

fun main() {
    val state = State()
    try {
        while (true) {
            print(INPUT_PREFIX)

            val input = readln().trim()
            if (input.isEmpty()) {
                continue
            }
            when (ACTIONS[input]) {
                KeywordAction.Exit -> {
                    print('\n')
                    break
                }

                KeywordAction.Reset -> {
                    state.clear()
                    continue
                }

                else -> {}
            }

            val tokens = input.asTokenSequence()
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
