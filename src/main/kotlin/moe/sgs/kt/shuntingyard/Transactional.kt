package moe.sgs.kt.shuntingyard

/**
 * Interface to facilitate Transactional behavior
 *
 */
interface Transactional {
    fun isTransactionActive(): Boolean

    /**
     * apply all changes
     *
     */
    fun commit()

    /**
     * reverts all changes
     *
     */
    fun abort()
}

/**
 * Finalizes the [Transactional] by either committing or aborting it based on the state of the [Result].
 *
 * @param transactional [Transactional] to commit or abort on
 * @return [Result]
 */
inline fun <R> Result<R>.finalize(transactional: Transactional): Result<R> {
    return this.onFailure { transactional.abort() }.onSuccess { transactional.commit() }
}

/**
 * Finalizes the [Transactional] by either committing or aborting it based on the state of the [Result].
 *
 * @param result [Result] to check state on
 * @return [Transactional]
 */
inline fun <R> Transactional.finalize(result: Result<R>): Transactional {
    result.onFailure { this.abort() }.onSuccess { this.commit() }
    return this
}
