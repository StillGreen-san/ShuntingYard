package moe.sgs.kt.shuntingyard

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class ReversePolishSequenceTest {

    @Test
    fun operators() {
        val dac = DefaultArithmeticContext
        val seq = " 3+ 4 * 2/(1 -5)^2^ 3 ".asTokenSequence().toReversePolishSequence()
        val it = seq.iterator()
        assertEquals(Token.Number(3), it.next())
        assertEquals(Token.Number(4), it.next())
        assertEquals(Token.Number(2), it.next())
        assertEquals(Token.Operator(dac.operators["*"]!!), it.next())
        assertEquals(Token.Number(1), it.next())
        assertEquals(Token.Number(5), it.next())
        assertEquals(Token.Operator(dac.operators["-"]!!), it.next())
        assertEquals(Token.Number(2), it.next())
        assertEquals(Token.Number(3), it.next())
        assertEquals(Token.Operator(dac.operators["^"]!!), it.next())
        assertEquals(Token.Operator(dac.operators["^"]!!), it.next())
        assertEquals(Token.Operator(dac.operators["/"]!!), it.next())
        assertEquals(Token.Operator(dac.operators["+"]!!), it.next())
        assertFalse(it.hasNext())
        assertThrows(NoSuchElementException::class.java) { it.next() }
    }

    @Test
    fun identifiers() {
        val dac = DefaultArithmeticContext
        val seq = "log(max(2,3)/3*Pi)".asTokenSequence().toReversePolishSequence()
        val it = seq.iterator()
        assertEquals(Token.Number(2), it.next())
        assertEquals(Token.Number(3), it.next())
        assertEquals(Token.Function(dac.functions["max"]!!), it.next())
        assertEquals(Token.Number(3), it.next())
        assertEquals(Token.Operator(dac.operators["/"]!!), it.next())
        assertEquals(Token.Number(dac.identifiers["Pi"]!!, "Pi"), it.next())
        assertEquals(Token.Operator(dac.operators["*"]!!), it.next())
        assertEquals(Token.Function(dac.functions["log"]!!), it.next())
        assertFalse(it.hasNext())
        assertThrows(NoSuchElementException::class.java) { it.next() }
    }

    @Test
    fun failure() {
        val dac = DefaultArithmeticContext
        val seq = "3+4)*2/(1-5)^2^3".asTokenSequence().toReversePolishSequence()
        val it = seq.iterator()
        assertEquals(Token.Number(3), it.next())
        assertEquals(Token.Number(4), it.next())
        assertEquals(Token.Operator(dac.operators["+"]!!), it.next())
        assertFalse(it.hasNext())
        assertThrows(IllegalArgumentException::class.java) { it.next() }
    }
}
