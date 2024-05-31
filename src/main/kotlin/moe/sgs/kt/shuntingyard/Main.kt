package moe.sgs.kt.shuntingyard

import kotlin.math.max

const val INPUT_PREFIX = "|> "
const val OUTPUT_PREFIX = "|: "
const val ACTION_HEADER = "Action"
const val KEYWORD_HEADER = "Keywords"

enum class KeywordAction {
    Exit,
    Reset,
    Help,
}

val ACTIONS = mapOf(
    "quit" to KeywordAction.Exit,
    "exit" to KeywordAction.Exit,
    ":q" to KeywordAction.Exit,
    "reset" to KeywordAction.Reset,
    "clear" to KeywordAction.Reset,
    "help" to KeywordAction.Help,
    "?" to KeywordAction.Help,
)

fun displayHelp() {
    val maxNameLen = max(KeywordAction.entries.maxOf { it.name.length }, ACTION_HEADER.length)
    println("${OUTPUT_PREFIX}${ACTION_HEADER.padEnd(maxNameLen)} : $KEYWORD_HEADER")
    val actionGroups = ACTIONS.entries.groupBy({ it.value }) { it.key }
    for (entry in KeywordAction.entries) {
        println("$OUTPUT_PREFIX${entry.name.padEnd(maxNameLen)} : ${actionGroups[entry]!!.joinToString()}")
    }
}

fun main() {
    val state = State()
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

            KeywordAction.Help -> {
                displayHelp()
                continue
            }

            null -> {}
        }

        val tokens = input.asTokenSequence()
        val rpn = tokens.toReversePolishSequence()
        val result = solve(rpn, state) //TODO make transactional (to "revert" if err)

        print(OUTPUT_PREFIX)
        println(result.getOrElse { result.exceptionOrNull()!!.localizedMessage })
    }
}

//TODO fix "1 1+", "1 1-", "= 1" inputs
