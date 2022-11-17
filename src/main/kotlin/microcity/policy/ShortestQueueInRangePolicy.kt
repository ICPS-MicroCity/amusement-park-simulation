package microcity.policy

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Devices.getCoordinates
import microcity.Queues.Queue
import microcity.Utils.Visitors.getDestination
import microcity.Utils.Visitors.getQueues
import org.protelis.lang.datatype.Tuple
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * A policy that chooses the next destination based on the length of the queues and within a range.
 */
class ShortestQueueInRangePolicy(private val range: Double = 0.001) : NextPolicy {
    override fun getNext(ctx: AlchemistExecutionContext<*>): Tuple =
        getQueues(ctx).filter { getDestination(ctx) != it.attraction.position.coordinates }
            .filter { withinRange(it, ctx) }
            .filter { it.visitors.size == getQueues(ctx).minByOrNull { q -> q.visitors.size }?.visitors?.size }
            .let {
                when (it.isNotEmpty()) {
                    true -> it[Random.nextInt(0, it.size)].attraction.position.coordinates
                    else -> ShortestQueuePolicy().getNext(ctx)
                }
            }

    private fun withinRange(queue: Queue, ctx: AlchemistExecutionContext<*>): Boolean =
        sqrt(
            (getCoordinates(ctx).getAsDouble(0) - queue.attraction.position.coordinates.getAsDouble(0)).pow(2.0) +
                (getCoordinates(ctx).getAsDouble(1) - queue.attraction.position.coordinates.getAsDouble(1)).pow(2.0)
        ) < this.range

    private fun Tuple.getAsDouble(index: Int): Double = this.get(index) as Double
}
