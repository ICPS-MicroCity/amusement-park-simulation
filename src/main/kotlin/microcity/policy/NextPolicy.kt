package microcity.policy

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import org.protelis.lang.datatype.Tuple

interface NextPolicy {
    fun getNext(ctx: AlchemistExecutionContext<*>, current: Tuple): Tuple
}
