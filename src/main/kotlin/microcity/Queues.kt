package microcity

import it.unibo.alchemist.model.implementations.times.DoubleTime
import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Device.get
import microcity.Device.getCoordinates
import microcity.Device.getId
import microcity.Positions.Attraction
import microcity.Positions.Position
import microcity.Positions.createPositions
import microcity.Utils.Attractions.getCapacity
import microcity.Utils.Attractions.getDuration
import microcity.Utils.Attractions.getPopularity
import microcity.Utils.Attractions.getQueue
import microcity.Utils.Attractions.getSatisfied
import microcity.Utils.Molecules.ATTRACTION
import microcity.Utils.Molecules.VISITOR
import microcity.Utils.Molecules.WAITING_TIME
import microcity.Utils.Visitors.getCardinality
import microcity.Utils.Visitors.getDestination
import microcity.Utils.Visitors.satisfy
import microcity.Utils.role
import org.protelis.lang.datatype.Tuple

object Queues {
    data class Queue(val attraction: Attraction, var visitors: List<Visitor>)
    data class Visitor(val position: Position, val destination: Tuple, val cardinality: Int)

    @JvmStatic
    fun createVisitors(ctx: AlchemistExecutionContext<*>): List<Visitor> = when {
        role(ctx, VISITOR) -> arrayListOf(Visitor(createPositions(ctx)[0], getDestination(ctx), getCardinality(ctx)))
        else -> arrayListOf()
    }

    @JvmStatic
    fun createQueue(ctx: AlchemistExecutionContext<*>): List<Queue> = when {
        role(ctx, ATTRACTION) -> arrayListOf(
            Queue(
                Attraction(
                    Position(getId(ctx), getCoordinates(ctx)),
                    getPopularity(ctx),
                    getCapacity(ctx),
                    getDuration(ctx)
                ),
                getQueue(ctx)
            )
        )
        else -> ArrayList()
    }

    @JvmStatic
    fun queuesUnion(a: List<Queue>, b: List<Queue>): List<Queue> = ArrayList(
        b.filter { a.find { q -> q.attraction.position.id == it.attraction.position.id } == null }
            .union(a).toList().sortedBy { it.attraction.position.id }
    )

    @JvmStatic
    fun dequeue(ctx: AlchemistExecutionContext<*>): List<Visitor> = when {
        role(ctx, ATTRACTION) && getQueue(ctx).isNotEmpty() ->
            getQueue(ctx).mapIndexed { index, visitor ->
                Pair(visitor, getQueue(ctx).take(index + 1).sumOf { v -> v.cardinality })
            }.takeWhile { it.second < getCapacity(ctx) }.map { it.first }
        else -> arrayListOf()
    }

    @JvmStatic
    fun addSatisfaction(ctx: AlchemistExecutionContext<*>, value: Boolean) {
        if (value) {
            Device.put(ctx, Utils.Molecules.SATISFACTIONS, Utils.Visitors.getSatisfactions(ctx) + 1)
        }
        satisfy(ctx, value)
    }

    @JvmStatic
    fun queueUnion(ctx: AlchemistExecutionContext<*>, a: List<Visitor>, b: List<Visitor>): List<Visitor> = when {
        role(ctx, ATTRACTION) -> b.union(a)
            .filter { it.position.coordinates == getCoordinates(ctx) }
            .filter { it.destination == getCoordinates(ctx) }
            .filter { it.position.id != getId(ctx) }
            .filterNot { getSatisfied(ctx).map { p -> p.position.id }.contains(it.position.id) }
        else -> createVisitors(ctx)
    }

    @JvmStatic
    fun satisfactionUnion(ctx: AlchemistExecutionContext<*>, a: List<Visitor>, b: List<Visitor>): List<Visitor> =
        ArrayList(
            when {
                role(ctx, VISITOR) -> a.union(b).filter { it.position.id == getId(ctx) }
                else -> a.union(b).toList().filter { it.position.coordinates == getCoordinates(ctx) }
            }
        )

    @JvmStatic
    fun updateWaitingTime(ctx: AlchemistExecutionContext<*>, time: DoubleTime) {
        if (role(ctx, VISITOR) && !(get(ctx, "moving") as Boolean)) {
            Device.put(
                ctx,
                WAITING_TIME,
                DoubleTime(get(ctx, WAITING_TIME) as Double).plus(
                    DoubleTime(ctx.currentTime.toDouble()).minus(time)
                ).toDouble()
            )
        }
    }
}
