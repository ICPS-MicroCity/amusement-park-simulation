package microcity.policy

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import org.protelis.lang.datatype.Tuple

/**
 * Common interface to choose the next destination.
 */
interface NextPolicy {
    fun getNext(ctx: AlchemistExecutionContext<*>): Tuple
}
