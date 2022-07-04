package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Device.getCoordinates
import microcity.Positions.getPositions
import microcity.Utils.Visitors.isSatisfied
import microcity.Utils.Visitors.satisfy
import org.protelis.lang.datatype.Tuple
import kotlin.random.Random

object Destinations {

    fun getDestination(ctx: AlchemistExecutionContext<*>): Tuple =
        Utils.Visitors.getDestination(ctx)

    @JvmStatic
    fun getNext(ctx: AlchemistExecutionContext<*>, current: Tuple): Tuple = when {
        isSatisfied(ctx) && getPositions(ctx).isNotEmpty() ->
            getPositions(ctx)[Random.nextInt(0, getPositions(ctx).size)].coordinates
        else -> current
    }

    @JvmStatic
    fun satisfy(ctx: AlchemistExecutionContext<*>, currentDestination: Tuple) {
        satisfy(ctx, currentDestination == getCoordinates(ctx))
    }

}