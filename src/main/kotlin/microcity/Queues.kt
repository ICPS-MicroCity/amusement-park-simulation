package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Device.getCoordinates
import microcity.Device.getId
import microcity.Positions.Position
import microcity.Utils.Molecules.ACTIVITY
import microcity.Utils.role
import microcity.Utils.Guests.enqueue
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
}