package microcity

import it.unibo.alchemist.model.implementations.times.DoubleTime
import it.unibo.alchemist.protelis.AlchemistExecutionContext
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
import microcity.Utils.Visitors.getQueues
import microcity.Utils.role

object Queues {
    data class Queue(val attraction: Position, var visitors: List<Position>)

    @JvmStatic
    fun createQueue(ctx: AlchemistExecutionContext<*>): List<Queue> = when {
        role(ctx, ATTRACTION) -> arrayListOf(Queue(Position(getId(ctx), getCoordinates(ctx)), getQueue(ctx)))
        else -> ArrayList()
    }

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
    fun queueUnion(ctx: AlchemistExecutionContext<*>, a: List<Position>, b: List<Position>): List<Position> = when {
        role(ctx, ATTRACTION) -> b.union(a)
            .filter { it.coordinates == getCoordinates(ctx) }
            .filter { it.id != getId(ctx) }
            .filterNot { getSatisfied(ctx).map { p -> p.id }.contains(it.id) }
        else -> createPositions(ctx)
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
        if (role(ctx, VISITOR) && getQueues(ctx).any { q -> q.visitors.any { v -> v.id == getId(ctx) } }) {
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
