package microcity

import it.unibo.alchemist.model.implementations.times.DoubleTime
import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Device.get
import microcity.Device.getCoordinates
import microcity.Device.getId
import microcity.Positions.Position
import microcity.Positions.createPositions
import microcity.Utils.Attractions.getQueue
import microcity.Utils.Attractions.getSatisfied
import microcity.Utils.Attractions.getVisitorsPerRound
import microcity.Utils.Molecules.ATTRACTION
import microcity.Utils.Molecules.VISITOR
import microcity.Utils.Molecules.WAITING_TIME
import microcity.Utils.Visitors.getDestination
import microcity.Utils.Visitors.getQueues
import microcity.Utils.role
import org.protelis.lang.datatype.Tuple

object Queues {
    data class Queue(val attraction: Position, var visitors: List<Position>)
    data class Visitor(val position: Position, val destination: Tuple)

    @JvmStatic
    fun createVisitors(ctx: AlchemistExecutionContext<*>): List<Visitor> = when {
        role(ctx, VISITOR) -> arrayListOf(Visitor(createPositions(ctx)[0], getDestination(ctx)))
        else -> arrayListOf()
    }

    @JvmStatic
    fun createQueue(ctx: AlchemistExecutionContext<*>): List<Queue> = when {
        role(ctx, ATTRACTION) -> arrayListOf(Queue(Position(getId(ctx), getCoordinates(ctx)), getQueue(ctx)))
        else -> ArrayList()
    }

    @JvmStatic
    fun flatting(a: List<Queue>, b: List<Queue>): List<Queue> =
        a.filter { b.find { q -> q.attraction.id == it.attraction.id } == null }
            .union(b).toList().sortedBy { it.attraction.id }

    @JvmStatic
    fun queuesUnion(ctx: AlchemistExecutionContext<*>, l1: List<Queue>, l2: List<Queue>): List<Queue> =
        ArrayList(
            l1.filter { l2.find { q -> q.attraction.id == it.attraction.id } == null }
                .union(l2)
                .toList()
                .sortedBy { it.attraction.id }
        ).apply {
            this.find { it.attraction.id == getId(ctx) }?.visitors = getQueue(ctx)
        }

    @JvmStatic
    fun dequeue(ctx: AlchemistExecutionContext<*>): List<Position> = when {
        role(
            ctx,
            ATTRACTION
        ) -> ArrayList(getQueue(ctx).take(getVisitorsPerRound(ctx).coerceAtMost(getQueue(ctx).size)))
        else -> arrayListOf()
    }

    @JvmStatic
    fun addSatisfaction(ctx: AlchemistExecutionContext<*>, value: Boolean) {
        if (value) {
            Device.put(ctx, Utils.Molecules.SATISFACTIONS, Utils.Visitors.getSatisfactions(ctx) + 1)
        }
    }

    @JvmStatic
    fun queueUnion(ctx: AlchemistExecutionContext<*>, a: List<Visitor>, b: List<Visitor>): List<Visitor> = when {
        role(ctx, ATTRACTION) -> b.union(a)
            .filter { it.position.coordinates == getCoordinates(ctx) }
            .filter { it.destination == getCoordinates(ctx) }
            .filter { it.position.id != getId(ctx) }
            .filterNot { getSatisfied(ctx).map { p -> p.id }.contains(it.position.id) }
        else -> createVisitors(ctx)
    }

    @JvmStatic
    fun satisfactionUnion(ctx: AlchemistExecutionContext<*>, a: List<Position>, b: List<Position>): List<Position> =
        ArrayList(
            when {
                role(ctx, VISITOR) -> a.union(b).filter { it.id == getId(ctx) }
                else -> a.union(b).toList().filter { it.coordinates == getCoordinates(ctx) }
            }
        )

    @JvmStatic
    fun updateWaitingTime(ctx: AlchemistExecutionContext<*>, time: DoubleTime) {
        if (role(ctx, VISITOR) && !(get(ctx, "moving") as Boolean)) {
            Device.put(
                ctx,
                WAITING_TIME,
                DoubleTime(Device.get(ctx, WAITING_TIME) as Double).plus(
                    DoubleTime(ctx.currentTime.toDouble()).minus(time)
                ).toDouble()
            )
        }
    }
}
