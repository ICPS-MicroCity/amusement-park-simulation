package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Device.getCoordinates
import microcity.Positions.getPositions
import microcity.Utils.Visitors.getDestination
import microcity.Utils.Visitors.getNextPolicy
import microcity.Utils.Visitors.getQueues
import microcity.Utils.Visitors.isSatisfied
import microcity.Utils.Visitors.satisfy
import microcity.Utils.Visitors.setPreviousDestination
import org.protelis.lang.datatype.Tuple

object Destinations {

    @JvmStatic
    fun getNext(ctx: AlchemistExecutionContext<*>, current: Tuple): Tuple = when {
        isSatisfied(ctx) && getPositions(ctx).size > 10 && getQueues(ctx).size > 1 ->
            getNextPolicy(ctx).getNext(ctx).also {
                if (it != getDestination(ctx))
                    setPreviousDestination(ctx, getDestination(ctx))
            }
        else -> current
    }

    @JvmStatic
    fun dissatisfy(ctx: AlchemistExecutionContext<*>, currentDestination: Tuple) {
        satisfy(ctx, currentDestination == getCoordinates(ctx))
    }

    @JvmStatic
    fun getRecommendation(ctx: AlchemistExecutionContext<*>): Tuple {
        getNextPolicy(ctx)
    }
}
