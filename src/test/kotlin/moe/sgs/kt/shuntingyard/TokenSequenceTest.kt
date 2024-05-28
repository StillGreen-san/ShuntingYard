package moe.sgs.kt.shuntingyard

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class TokenSequenceTest {

    @Test
    fun operators() {
        val dac = DefaultArithmeticContext
        val seq = "3 + 4*2 / ( 1-5) ^2^3".asTokenSequence()
        val it = seq.iterator()
        assertEquals(Token.Number(3), it.next())
        assertEquals(Token.Operator(dac.operators["+"]!!), it.next())
        assertEquals(Token.Number(4), it.next())
        assertEquals(Token.Operator(dac.operators["*"]!!), it.next())
        assertEquals(Token.Number(2), it.next())
        assertEquals(Token.Operator(dac.operators["/"]!!), it.next())
        assertEquals(Token.OpenParen(), it.next())
        assertEquals(Token.Number(1), it.next())
        assertEquals(Token.Operator(dac.operators["-"]!!), it.next())
        assertEquals(Token.Number(5), it.next())
        assertEquals(Token.CloseParen(), it.next())
        assertEquals(Token.Operator(dac.operators["^"]!!), it.next())
        assertEquals(Token.Number(2), it.next())
        assertEquals(Token.Operator(dac.operators["^"]!!), it.next())
        assertEquals(Token.Number(3), it.next())
        assertFalse(it.hasNext())
        assertThrows(NoSuchElementException::class.java) { it.next() }
    }

    @Test
    fun identifiers() {
        val dac = DefaultArithmeticContext
        val seq = "sin ( max(2 ,3) / 3 *PI)".asTokenSequence()
        val it = seq.iterator()
        assertEquals(Token.Function(dac.functions["sin"]!!), it.next())
        assertEquals(Token.OpenParen(), it.next())
        assertEquals(Token.Function(dac.functions["max"]!!), it.next())
        assertEquals(Token.OpenParen(), it.next())
        assertEquals(Token.Number(2), it.next())
        assertEquals(Token.Komma(), it.next())
        assertEquals(Token.Number(3), it.next())
        assertEquals(Token.CloseParen(), it.next())
        assertEquals(Token.Operator(dac.operators["/"]!!), it.next())
        assertEquals(Token.Number(3), it.next())
        assertEquals(Token.Operator(dac.operators["*"]!!), it.next())
        assertEquals(Token.Value("PI"), it.next())
        assertEquals(Token.CloseParen(), it.next())
        assertFalse(it.hasNext())
        assertThrows(NoSuchElementException::class.java) { it.next() }
    }

    @Test
    fun failure() {
        val ac = object : ArithmeticContext {
            override val functions: Map<String, TokenFunctionData> = mapOf()
            override val operators: Map<String, TokenOperatorData> = mapOf()
        }
        val seq = "3+4*2/(1-5)^2^3".asTokenSequence(ac)
        val it = seq.iterator()
        assertEquals(Token.Number(3), it.next())
        assertFalse(it.hasNext())
        assertThrows(InputMismatchException::class.java) { it.next() }
    }
}
