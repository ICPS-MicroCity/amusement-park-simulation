package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Destinations.changeDestination
import microcity.Devices.get
import microcity.Devices.has
import microcity.Devices.put
import microcity.Utils.Molecules.RECOMMENDATION
import microcity.Utils.Visitors.isMoving
import org.protelis.lang.datatype.Tuple
import org.protelis.lang.datatype.impl.ArrayTupleImpl
import kotlin.random.Random

object Recommendations {

    /**
     * Recommend the next attraction to visit.
     */
    @JvmStatic
    fun recommend(ctx: AlchemistExecutionContext<*>): Tuple = when {
        Positions.attractionsPositions(ctx).size > 10 && Utils.Visitors.getQueues(ctx).size > 1 -> Utils.Visitors.getRecommendationPolicy(ctx).getNext(ctx)
        else -> ArrayTupleImpl()
    }

    /**
     * Gets the current recommendation.
     */
    @JvmStatic
    fun getRecommendation(ctx: AlchemistExecutionContext<*>): Tuple = when {
        has(ctx, RECOMMENDATION) -> get(ctx, RECOMMENDATION) as Tuple
        else -> ArrayTupleImpl()
    }

    /**
     * Empties the concentration of the RECOMMENDATION molecule.
     */
    @JvmStatic
    fun emptyRecommendation(ctx: AlchemistExecutionContext<*>) {
        put(ctx, RECOMMENDATION, ArrayTupleImpl())
    }

    /**
     * Evaluate whether to accept the recommendation or not.
     */
    @JvmStatic
    fun evaluateRecommendation(ctx: AlchemistExecutionContext<*>) {
        if (isMoving(ctx) && !getRecommendation(ctx).isEmpty && Random.nextDouble() > 0.5) {
            changeDestination(ctx, getRecommendation(ctx))
        }
    }
}
