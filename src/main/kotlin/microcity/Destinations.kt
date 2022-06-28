package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Device.getCoordinates
import microcity.Positions.getPositions
import microcity.Utils.Guests.isSatisfied
import microcity.Utils.Guests.satisfy
import org.protelis.lang.datatype.Tuple
import kotlin.random.Random

object Destinations {

    @JvmStatic
    fun getDestination(ctx: AlchemistExecutionContext<*>): Tuple =
        Utils.Guests.getDestination(ctx)

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