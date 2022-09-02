package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Destinations.changeDestination
import microcity.Device.get
import microcity.Device.has
import microcity.Device.put
import microcity.Utils.Molecules.RECOMMENDATION
import org.protelis.lang.datatype.Tuple
import org.protelis.lang.datatype.impl.ArrayTupleImpl
import kotlin.random.Random

object Recommendations {

    @JvmStatic
    fun recommend(ctx: AlchemistExecutionContext<*>): Tuple = when {
        Positions.getPositions(ctx).size > 10 && Utils.Visitors.getQueues(ctx).size > 1 -> Utils.Visitors.getRecommendationPolicy(ctx).getNext(ctx)
        else -> Device.getCoordinates(ctx)
    }

    @JvmStatic
    fun getRecommendation(ctx: AlchemistExecutionContext<*>): Tuple = when {
        has(ctx, RECOMMENDATION) -> get(ctx, RECOMMENDATION) as Tuple
        else -> ArrayTupleImpl()
    }

    @JvmStatic
    fun emptyRecommendation(ctx: AlchemistExecutionContext<*>) {
        put(ctx, RECOMMENDATION, ArrayTupleImpl())
    }

    @JvmStatic
    fun evaluateRecommendation(ctx: AlchemistExecutionContext<*>) {
        when {
            !getRecommendation(ctx).isEmpty && Random.nextDouble() > 0.5 -> changeDestination(ctx, getRecommendation(ctx))
            else -> emptyRecommendation(ctx)
        }
    }
}