package moe.sgs.kt.shuntingyard

import ch.obermuhlner.math.big.BigDecimalMath.pow
import ch.obermuhlner.math.big.BigDecimalMath.sin
import java.math.MathContext

class DefaultArithmeticContext : ArithmeticContext { //TODO make singleton
    val mathContext: MathContext = MathContext.DECIMAL128
    override val functions: Map<String, TokenFunctionData> = mapOf(
        "max" to TokenFunctionData.make { l, r -> l.max(r) },
        "min" to TokenFunctionData.make { l, r -> l.min(r) },
        "abs" to TokenFunctionData.make { l -> l.abs() },
        "sin" to TokenFunctionData.make { l -> sin(l, mathContext) },
    )
    override val operators: Map<String, TokenOperatorData> = mapOf(
        "=" to TokenOperatorData.make(1, Associativity.RightAssociative) { _, _ -> throw NoSuchMethodException() },
        "+" to TokenOperatorData.make(2, Associativity.LeftAssociative) { l, r -> l.add(r, mathContext) },
        "-" to TokenOperatorData.make(2, Associativity.LeftAssociative) { l, r -> l.subtract(r, mathContext) },
        "*" to TokenOperatorData.make(3, Associativity.LeftAssociative) { l, r -> l.multiply(r, mathContext) },
        "/" to TokenOperatorData.make(3, Associativity.LeftAssociative) { l, r -> l.divide(r, mathContext) },
        "^" to TokenOperatorData.make(4, Associativity.RightAssociative) { l, r -> pow(l, r, mathContext) },
    )
}
