package microcity.policy

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Utils.Visitors.getDestination
import microcity.Utils.Visitors.getQueues
import org.protelis.lang.datatype.Tuple

class ShortestQueuePolicy : NextPolicy {
    override fun getNext(ctx: AlchemistExecutionContext<*>): Tuple =
        getQueues(ctx).filter { getDestination(ctx) != it.attraction.coordinates }.sortedBy { it.visitors.size }[0].attraction.coordinates
}
