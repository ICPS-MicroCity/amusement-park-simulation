package microcity.policy

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Positions.getPositions
import microcity.Utils.Visitors.getPreviousDestination
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
                        ctx.routingDistance(it.attraction.position.coordinates),
                        getPositions(ctx).map { p -> ctx.routingDistance(p.position.coordinates) }
                    ) * queueTimeScore(queueTime(it.visitors.size, it.attraction.capacity, it.attraction.duration.toDouble()))
                ).considerPreviousDestination(it.attraction.position.coordinates, getPreviousDestination(ctx))
            )
        }.maxBy { it.second }.first.position.coordinates
}
