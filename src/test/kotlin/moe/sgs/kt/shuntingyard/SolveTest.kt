// SPDX-License-Identifier: MPL-2.0

// SPDX-License-Identifier: MPL-2.0

// SPDX-License-Identifier: MPL-2.0

package moe.sgs.kt.shuntingyard

import ch.obermuhlner.math.big.BigDecimalMath
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*

fun String.MAKE_SEQUENCE_FOR_TEST(): ReversePolishSequence = ReversePolishSequence(this.asTokenSequence())

class SolveTest {

    @Test
    fun operators() {
        val seq = "3+4* 2/ (1-5)^ 2^3".MAKE_SEQUENCE_FOR_TEST()
        val res = solve(seq, State())
        assertTrue(res.isSuccess)
        assertEquals(BigDecimal("3.0001220703125"), res.getOrThrow())
    }

    @Test
    fun identifiers() {
        val seq = "log (max(two , 3 )/3*Pi )".MAKE_SEQUENCE_FOR_TEST()
        val state = State()
        state.identifiers["two"] = BigDecimal(2)
        val res = solve(seq, state)
        assertTrue(res.isSuccess)
        assertEquals(BigDecimal("1.144729885849400174143427351353059"), res.getOrThrow())
    }

    @Test
    fun failure() {
        val seq = "(1 +-1))+1".MAKE_SEQUENCE_FOR_TEST()
        val res = solve(seq, State())
        assertTrue(res.isFailure)
        assertTrue(res.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun surrounded() {
        val res = solve("(1)".MAKE_SEQUENCE_FOR_TEST(), State())
        assertEquals(BigDecimal(1), res.getOrThrow())
    }

    @Test
    fun extraParen() {
        val res = solve("1+(1+-1))".MAKE_SEQUENCE_FOR_TEST(), State())
        assertTrue(res.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun assignNothing() {
        val res1 = solve("=1".MAKE_SEQUENCE_FOR_TEST(), State())
        assertTrue(res1.exceptionOrNull() is InputMismatchException)

        val res2 = solve("x=1+(=1+1)".MAKE_SEQUENCE_FOR_TEST(), State())
        assertTrue(res2.exceptionOrNull() is InputMismatchException)
    }

    @Test
    fun assign() {
        val state = State()
        val res1 = solve("x=1".MAKE_SEQUENCE_FOR_TEST(), state)
        assertEquals(BigDecimal(1), res1.getOrNull())
        assertEquals(BigDecimal(1), state.identifiers["x"])

        val res3 = solve("x=1+(y=1+x)".MAKE_SEQUENCE_FOR_TEST(), state)
        assertEquals(BigDecimal(3), res3.getOrNull())
        assertEquals(BigDecimal(3), state.identifiers["x"])
        assertEquals(BigDecimal(2), state.identifiers["y"])
    }
}
