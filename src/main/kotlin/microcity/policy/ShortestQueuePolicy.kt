package microcity.policy

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Utils.Visitors.getDestination
import microcity.Utils.Visitors.getQueues
import org.protelis.lang.datatype.Tuple
import kotlin.random.Random

class ShortestQueuePolicy : NextPolicy {
    override fun getNext(ctx: AlchemistExecutionContext<*>): Tuple =
        getQueues(ctx).filter { getDestination(ctx) != it.attraction.position.coordinates }
            .filter { it.visitors.size == getQueues(ctx).minBy { q -> q.visitors.size }.visitors.size }
            .let { it[Random.nextInt(0, it.size)] }
            .attraction.position.coordinates
}
