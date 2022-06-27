package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Device.getCoordinates
import microcity.Device.getId
import microcity.Positions.Position
import microcity.Utils.Activities.getGuestsPerRound
import microcity.Utils.Activities.getQueue
import microcity.Utils.Activities.updateQueue
import microcity.Utils.Molecules.ACTIVITY
import microcity.Utils.role
import microcity.Utils.Guests.enqueue
import microcity.Utils.Guests.isSatisfied
import microcity.Utils.Molecules.GUEST
import org.protelis.lang.datatype.Tuple

object Queues {
    @JvmStatic
    fun enqueue(ctx: AlchemistExecutionContext<*>, destination: Tuple) {
        enqueue(ctx, destination == getCoordinates(ctx))
    }

    @JvmStatic
    fun queueUnion(ctx: AlchemistExecutionContext<*>, a: List<Position>, b: List<Position>): List<Position> = when {
        role(ctx, ACTIVITY) -> b.union(a)
            .filter { it.coordinates == getCoordinates(ctx) }
            .filter { it.id != getId(ctx) }
        else -> arrayListOf(Position(getId(ctx), getCoordinates(ctx)))
    }

    @JvmStatic
    fun satisfyGuests(ctx: AlchemistExecutionContext<*>): List<Position> = when {
        role(ctx, ACTIVITY) -> ArrayList(getQueue(ctx).take(getGuestsPerRound(ctx)))
        else -> arrayListOf()
    }

    @JvmStatic
    fun satisfactionUnion(ctx: AlchemistExecutionContext<*>, a: List<Position>, b: List<Position>): List<Position> = ArrayList(when {
        role(ctx, GUEST) -> a.union(b).filter { it.id == getId(ctx) }
        else -> a.union(b).toList()
    })




}