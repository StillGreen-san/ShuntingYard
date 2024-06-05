// SPDX-License-Identifier: MPL-2.0

package moe.sgs.kt.shuntingyard

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TransactionalHashMapTest {

    @Test
    fun containsKey() {
        val map = transactionalHashMapOf("A" to 1, "B" to 2)
        assertTrue(map.containsKey("A"))
        assertFalse(map.containsKey("C"))
        assertFalse(map.isTransactionActive())

        map.abort()
        assertTrue(map.containsKey("A"))
        assertFalse(map.isTransactionActive())
    }

    @Test
    fun get() {
        val map = transactionalHashMapOf("A" to 1, "B" to 2)
        assertEquals(1, map["A"])
        assertNull(map["C"])
        assertTrue(map.isTransactionActive())

        map.commit()
        assertTrue(map.containsKey("B"))
        assertFalse(map.isTransactionActive())
    }

    @Test
    fun put() {
        val map = transactionalHashMapOf<String, Int>()
        map["A"] = 1
        map["B"] = 2
        assertEquals(1, map["A"])
        assertNull(map["C"])
        assertTrue(map.isTransactionActive())

        map.commit()
        map.abort()
        assertFalse(map.isTransactionActive())
        assertEquals(2, map["B"])
    }
}
