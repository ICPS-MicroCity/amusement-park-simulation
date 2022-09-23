package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Device.getCoordinates
import microcity.Device.put
import microcity.Positions.getPositions
import microcity.Recommendations.emptyRecommendation
import microcity.Recommendations.getRecommendation
import microcity.Utils.Molecules.DESTINATION
import microcity.Utils.Visitors.getNextPolicy
import microcity.Utils.Visitors.getPreviousDestination
import microcity.Utils.Visitors.getQueues
import microcity.Utils.Visitors.isMoving
import microcity.Utils.Visitors.isSatisfied
import microcity.Utils.Visitors.satisfy
import org.protelis.lang.datatype.Tuple
import kotlin.random.Random

object Destinations {

    @JvmStatic
    fun getNext(ctx: AlchemistExecutionContext<*>, current: Tuple): Tuple = when {
        isSatisfied(ctx) && getPositions(ctx).size > 10 && getQueues(ctx).size > 1 -> getNextPolicy(ctx).getNext(ctx)
        isMoving(ctx) && !getRecommendation(ctx).isEmpty && getRecommendation(ctx) != getPreviousDestination(ctx) && Random.nextDouble() > 0.5 ->
            getRecommendation(ctx).also { emptyRecommendation(ctx) }
        else -> current
    }

    @JvmStatic
    fun dissatisfy(ctx: AlchemistExecutionContext<*>, currentDestination: Tuple) {
        satisfy(ctx, currentDestination == getCoordinates(ctx))
    }

    @JvmStatic
    fun changeDestination(ctx: AlchemistExecutionContext<*>, dst: Tuple) {
        put(ctx, DESTINATION, dst)
    }
}
