package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Device.getCoordinates
import microcity.Positions.getPositions
import microcity.Utils.Visitors.getNextPolicy
import microcity.Utils.Visitors.getQueues
import microcity.Utils.Visitors.isSatisfied
import microcity.Utils.Visitors.satisfy
import org.protelis.lang.datatype.Tuple

object Destinations {

    fun getDestination(ctx: AlchemistExecutionContext<*>): Tuple =
        Utils.Visitors.getDestination(ctx)

    @JvmStatic
    fun getNext(ctx: AlchemistExecutionContext<*>, current: Tuple): Tuple = when {
        isSatisfied(ctx) && getPositions(ctx).isNotEmpty() && getQueues(ctx).size > 1 -> getNextPolicy(ctx).getNext(ctx)
        else -> current
    }

    @JvmStatic
    fun dissatisfy(ctx: AlchemistExecutionContext<*>, currentDestination: Tuple) {
        satisfy(ctx, currentDestination == getCoordinates(ctx))
    }
}
