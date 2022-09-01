package microcity.policy

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Positions.getPositions
import microcity.Utils.Visitors.getQueues
import org.protelis.lang.datatype.Tuple

class SituatedRecommendationPolicy : NextPolicy {
    override fun getNext(ctx: AlchemistExecutionContext<*>): Tuple =
        getQueues(ctx).map {
            Pair(
                it.attraction,
                likelihood(
                    it.attraction.popularity.toDouble(),
                    lazinessFromDistance(
                        ctx.routingDistance(it.attraction.coordinates),
                        getPositions(ctx).map { p -> ctx.routingDistance(p.coordinates) }
                    ) + it.visitors.size.toDouble()
                ))
        }.maxBy { it.second }.first.coordinates
}