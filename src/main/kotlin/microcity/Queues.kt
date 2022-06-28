package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Device.getCoordinates
import microcity.Device.getId
import microcity.Positions.Position
import microcity.Positions.createPositions
import microcity.Utils.Activities.getGuestsPerRound
import microcity.Utils.Activities.getQueue
import microcity.Utils.Guests.getDestination
import microcity.Utils.Guests.isGuest
import microcity.Utils.Molecules.ACTIVITY
import microcity.Utils.Molecules.GUEST
import microcity.Utils.role

object Queues {

    @JvmStatic
    fun dequeue(ctx: AlchemistExecutionContext<*>): List<Position> = when {
        role(ctx, ACTIVITY) -> ArrayList(getQueue(ctx).take(getGuestsPerRound(ctx)))
        else -> arrayListOf()
    }

    @JvmStatic
    fun queueUnion(ctx: AlchemistExecutionContext<*>, a: List<Position>, b: List<Position>): List<Position> = when {
        role(ctx, ACTIVITY) -> b.union(a)
            .filter { it.coordinates == getCoordinates(ctx) }
            .filter { it.id != getId(ctx) }
        else -> createPositions(ctx)
    }

    @JvmStatic
    fun satisfactionUnion(ctx: AlchemistExecutionContext<*>, a: List<Position>, b: List<Position>): List<Position> = ArrayList(when {
        role(ctx, GUEST) -> a.union(b).filter { it.id == getId(ctx) }
        else -> a.union(b).toList().filter { it.coordinates == getCoordinates(ctx) }
    })
}