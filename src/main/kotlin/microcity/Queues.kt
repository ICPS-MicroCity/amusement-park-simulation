package microcity

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
import microcity.Utils.role

object Queues {

    @JvmStatic
    fun createQueue(ctx: AlchemistExecutionContext<*>): List<Position> = when {
        role(ctx, ATTRACTION) -> getQueue(ctx)
        else -> ArrayList()
    }

    @JvmStatic
    fun queuesUnion(l1: List<Position>, l2: List<Position>): List<Position> =
        ArrayList(l1.union(l2).toList())

    @JvmStatic
    fun dequeue(ctx: AlchemistExecutionContext<*>): List<Position> = when {
        role(ctx, ATTRACTION) -> ArrayList(getQueue(ctx).take(getVisitorsPerRound(ctx).coerceAtMost(getQueue(ctx).size)))
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
    fun satisfactionUnion(ctx: AlchemistExecutionContext<*>, a: List<Position>, b: List<Position>): List<Position> = ArrayList(
        when {
            role(ctx, VISITOR) -> a.union(b).filter { it.id == getId(ctx) }
            else -> a.union(b).toList().filter { it.coordinates == getCoordinates(ctx) }
        }
    )
}
