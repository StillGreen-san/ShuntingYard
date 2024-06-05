// SPDX-License-Identifier: MPL-2.0

// SPDX-License-Identifier: MPL-2.0

// SPDX-License-Identifier: MPL-2.0

package moe.sgs.kt.shuntingyard

/**
 * Returns the second character.
 *
 * @throws NoSuchElementException if the char sequence has no two elements.
 */
fun CharSequence.second(): Char {
    if (length < 2) {
        throw NoSuchElementException("Char sequence has no second element.")
    }
    return this[1]
}

/**
 * Returns the second character, or `null` if the char sequence has not two elements.
 */
fun CharSequence.secondOrNull(): Char? {
    return if (length < 2) null else this[1]
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

/**
 * @return the name of the class, interface, or other entity represented by this `Class` object, without the fully
 * qualified package name.
 */
inline fun <T> Class<T>.nameNoPackage(): String {
    return this.name.substring(this.packageName.length + 1)
}

/**
 * @return the name of the class, interface, or other entity represented by this `Class` object, without the fully
 * qualified package name.
 */
inline fun <reified T> nameNoPackage(): String {
    return T::class.java.nameNoPackage()
}

/**
 * @return the name of the class, interface, or other entity represented by this `Class` object, without the fully
 * qualified package name.
 */
inline fun <reified T : Any> nameNoPackage(arg: T): String {
    return arg::class.java.nameNoPackage()
}
