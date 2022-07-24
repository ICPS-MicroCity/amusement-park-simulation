package microcity.policy

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Device.getCoordinates
import microcity.Queues.Queue
import microcity.Utils.Visitors.getQueues
import org.protelis.lang.datatype.Tuple
import kotlin.math.pow
import kotlin.math.sqrt

class ShortestQueueInRangePolicy : NextPolicy {
    private val range: Double = 0.001

    override fun getNext(ctx: AlchemistExecutionContext<*>): Tuple =
        getQueues(ctx).filter { withinRange(it, ctx) }.minByOrNull { it.visitors.size }?.attraction?.coordinates ?: ShortestQueuePolicy().getNext(ctx)

    private fun withinRange(queue: Queue, ctx: AlchemistExecutionContext<*>): Boolean =
        sqrt(
            (getCoordinates(ctx).getAsDouble(0) - queue.attraction.coordinates.getAsDouble(0)).pow(2.0) +
                (getCoordinates(ctx).getAsDouble(1) - queue.attraction.coordinates.getAsDouble(1)).pow(2.0)
        ) < range

    private fun Tuple.getAsDouble(index: Int): Double = this.get(index) as Double
}
