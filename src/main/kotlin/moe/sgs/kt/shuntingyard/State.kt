package moe.sgs.kt.shuntingyard

import java.math.BigDecimal

/**
 * State to persist between calls to [solve]
 *
 */
class State {
    val identifiers = HashMap<String, BigDecimal>()
}