// SPDX-License-Identifier: MPL-2.0

package moe.sgs.kt.shuntingyard

import java.math.BigDecimal

/**
 * State to persist between calls to [solve]
 *
 */
class State : Transactional {
    val identifiers = TransactionalHashMap<String, BigDecimal>()

    fun clear() {
        identifiers.clear()
    }

    override fun isTransactionActive(): Boolean = identifiers.isTransactionActive()

    override fun commit() = identifiers.commit()

    override fun abort() = identifiers.abort()
}
