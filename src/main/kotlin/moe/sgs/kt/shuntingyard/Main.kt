// SPDX-License-Identifier: MPL-2.0

// SPDX-License-Identifier: MPL-2.0

// SPDX-License-Identifier: MPL-2.0

// SPDX-License-Identifier: MPL-2.0

package moe.sgs.kt.shuntingyard

import java.math.BigDecimal
import kotlin.math.max

const val INPUT_PREFIX = "|> "
const val OUTPUT_PREFIX = "|: "
const val ACTION_HEADER = "Action"
const val KEYWORD_HEADER = "Keywords"

enum class KeywordAction {
    Exit,
    Reset,
    Help,
    Constants,
    Values,
    Functions,
}

enum class EvalAction {
    Exit,
    ReadNext,
    Solve,
}

val ACTIONS = mapOf(
    "quit" to KeywordAction.Exit,
    "exit" to KeywordAction.Exit,
    ":q" to KeywordAction.Exit,
    "reset" to KeywordAction.Reset,
    "clear" to KeywordAction.Reset,
    "help" to KeywordAction.Help,
    "?" to KeywordAction.Help,
    "constants" to KeywordAction.Constants,
    "const" to KeywordAction.Constants,
    "values" to KeywordAction.Values,
    "val" to KeywordAction.Values,
    "functions" to KeywordAction.Functions,
    "func" to KeywordAction.Functions,
)

fun displayHelp() {
    val maxNameLen = max(KeywordAction.entries.maxOf { it.name.length }, ACTION_HEADER.length)
    println("${OUTPUT_PREFIX}${ACTION_HEADER.padEnd(maxNameLen)} : $KEYWORD_HEADER")
    val actionGroups = ACTIONS.entries.groupBy({ it.value }) { it.key }
    for (entry in KeywordAction.entries) {
        println("$OUTPUT_PREFIX${entry.name.padEnd(maxNameLen)} : ${actionGroups[entry]!!.joinToString()}")
    }
}

fun printIdentifiers(entries: Set<Map.Entry<String, Any>>) {
    for ((key, value) in entries) {
        print(OUTPUT_PREFIX)
        when (value) {
            is BigDecimal -> println("$key = $value")
            is TokenFunctionData -> println("$key(${"arg, ".repeat(value.numArgs).removeSuffix(", ")})")
        }
    }
}

fun handleActions(input: String, state: State): EvalAction {
    when (ACTIONS[input]) {
        KeywordAction.Exit -> return EvalAction.Exit
        KeywordAction.Reset -> state.clear()
        KeywordAction.Help -> displayHelp()
        KeywordAction.Constants -> printIdentifiers(DefaultArithmeticContext.identifiers.entries)
        KeywordAction.Values -> printIdentifiers(state.identifiers.entries)
        KeywordAction.Functions -> printIdentifiers(DefaultArithmeticContext.functions.entries)
        null -> return EvalAction.Solve
    }
    return EvalAction.ReadNext
}

fun main() {
    val state = State()
    while (true) {
        print(INPUT_PREFIX)

        val input = readln().trim()
        if (input.isEmpty()) {
            continue
        }
        when (handleActions(input, state)) {
            EvalAction.Exit -> break
            EvalAction.ReadNext -> continue
            EvalAction.Solve -> {}
        }

        val tokens = input.asTokenSequence()
        val rpn = tokens.toReversePolishSequence()
        val result = solve(rpn, state)

        print(OUTPUT_PREFIX)
        println(result.getOrElse { result.exceptionOrNull()!!.localizedMessage })
    }
    print('\n')
}
