package moe.sgs.kt.shuntingyard

import java.util.NoSuchElementException

/**
 * Returns the second character.
 *
 * @throws NoSuchElementException if the char sequence is empty.
 */
fun CharSequence.second(): Char {
    if (length < 2) {
        throw NoSuchElementException("Char sequence is has no second element.")
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
