package moe.sgs.kt.shuntingyard

/**
 * Returns the second character.
 *
 * @throws NoSuchElementException if the char sequence is empty.
 */
fun CharSequence.second(): Char {
    if (length < 2) {
        throw NoSuchElementException("Char sequence has no second element.")
    }
    return this[1]
}

/**
 * Returns index of the first character matching the given [predicate], or [CharSequence.length] if the char sequence
 * does not contain such character.
 */
inline fun CharSequence.indexOfFirstOrLength(predicate: (Char) -> Boolean): Int {
    for (index in indices) {
        if (predicate(this[index])) {
            return index
        }
    }
    return length
}

/**
 * Calls the specified function [block] with `this` value as its receiver and returns its encapsulated result if invocation was successful,
 * catching any [Exception] exception that was thrown from the [block] function execution and encapsulating it as a failure.
 */
inline fun <T, R> T.tryCatch(block: T.() -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: Exception) {
        Result.failure(e)
    }
}

/**
 * Calls the specified function [block] and returns its encapsulated result if invocation was successful,
 * catching any [Exception] exception that was thrown from the [block] function execution and encapsulating it as a failure.
 */
inline fun <R> tryCatch(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: Exception) {
        Result.failure(e)
    }
}
