package moe.sgs.kt.shuntingyard

import ch.obermuhlner.math.big.BigDecimalMath
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*

class SolveTest {

    @Test
    fun operators() {
        val seq = "3+4* 2/ (1-5)^ 2^3".asTokenSequence().toReversePolishSequence()
        val res = solve(seq, State())
        assertTrue(res.isSuccess)
        assertEquals(BigDecimal("3.0001220703125"), res.getOrThrow())
    }

    @Test
    fun identifiers() {
        val seq = "log (max(two , 3 )/3*Pi )".asTokenSequence().toReversePolishSequence()
        val state = State()
        state.identifiers["two"] = BigDecimal(2)
        val res = solve(seq, state)
        assertTrue(res.isSuccess)
        assertEquals(BigDecimal("1.144729885849400174143427351353059"), res.getOrThrow())
    }

    @Test
    fun failure() {
        val seq = "(1 +-1))+1".asTokenSequence().toReversePolishSequence()
        val res = solve(seq, State())
        assertTrue(res.isFailure)
        assertTrue(res.exceptionOrNull() is IllegalArgumentException)
    }
}
