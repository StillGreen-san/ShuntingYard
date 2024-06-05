// SPDX-License-Identifier: MPL-2.0

// SPDX-License-Identifier: MPL-2.0

// SPDX-License-Identifier: MPL-2.0

// SPDX-License-Identifier: MPL-2.0

package moe.sgs.kt.shuntingyard

import ch.obermuhlner.math.big.BigDecimalMath.*
import ch.obermuhlner.math.big.kotlin.bigdecimal.root
import java.math.BigDecimal
import java.math.MathContext

private fun makeFunctionPair(
    name: String,
    func: (BigDecimal, BigDecimal) -> BigDecimal
): Pair<String, TokenFunctionData> {
    return name to TokenFunctionData.make(name, func)
}

private fun makeFunctionPair(
    name: String,
    func: (BigDecimal) -> BigDecimal
): Pair<String, TokenFunctionData> {
    return name to TokenFunctionData.make(name, func)
}

private fun makeOperatorPair(
    name: String,
    precedent: Int,
    associativity: Associativity,
    func: (BigDecimal, BigDecimal) -> BigDecimal
): Pair<String, TokenOperatorData> {
    return name to TokenOperatorData.make(name, precedent, associativity, func)
}

object DefaultArithmeticContext : ArithmeticContext {
    val mathContext: MathContext = MathContext.DECIMAL128
    override val identifiers: Map<String, BigDecimal> = mapOf(
        "Pi" to pi(mathContext),
        "π" to pi(mathContext),
        "\uD835\uDF0B" to pi(mathContext),
        "E" to e(mathContext),
        "Phi" to BigDecimal("1.61803398874989484820", mathContext),
    )
    override val functions: Map<String, TokenFunctionData> = mapOf(
        makeFunctionPair("max") { l, r -> l.max(r) },
        makeFunctionPair("min") { l, r -> l.min(r) },
        makeFunctionPair("abs") { l -> l.abs() },
        makeFunctionPair("root") { l, r -> root(l, r, mathContext) },
        makeFunctionPair("log") { l -> log(l, mathContext) },
        makeFunctionPair("sqrt") { l -> sqrt(l, mathContext) },
    )
    override val operators: Map<String, TokenOperatorData> = mapOf(
        makeOperatorPair("+", 2, Associativity.LeftAssociative) { l, r -> l.add(r, mathContext) },
        makeOperatorPair("-", 2, Associativity.LeftAssociative) { l, r -> l.subtract(r, mathContext) },
        makeOperatorPair("−", 2, Associativity.LeftAssociative) { l, r -> l.subtract(r, mathContext) },
        makeOperatorPair("*", 3, Associativity.LeftAssociative) { l, r -> l.multiply(r, mathContext) },
        makeOperatorPair("×", 3, Associativity.LeftAssociative) { l, r -> l.multiply(r, mathContext) },
        makeOperatorPair("/", 3, Associativity.LeftAssociative) { l, r -> l.divide(r, mathContext) },
        makeOperatorPair("÷", 3, Associativity.LeftAssociative) { l, r -> l.divide(r, mathContext) },
        makeOperatorPair("%", 3, Associativity.LeftAssociative) { l, r -> l.remainder(r, mathContext) },
        makeOperatorPair("^", 4, Associativity.RightAssociative) { l, r -> pow(l, r, mathContext) },
    )
}
