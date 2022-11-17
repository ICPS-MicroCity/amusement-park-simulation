package microcity.policy

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Positions.attractionsPositions
import microcity.Utils.Visitors.getDestination
import microcity.Utils.Visitors.getPreviousDestination
import microcity.Utils.Visitors.getQueues
import org.protelis.lang.datatype.Tuple

/**
 * A policy that chooses the next destination based on:
 *  - the popularity of the attraction
 *  - the waiting times considering the queues
 *  - the distance from the current position
 *  - whether the attraction is already visited
 */
class SituatedRecommendationPolicy : NextPolicy {
    override fun getNext(ctx: AlchemistExecutionContext<*>): Tuple =
        getQueues(ctx).map {
            Pair(
                it.attraction,
                likelihood(
                    it.attraction.popularity.toDouble(),
                    lazinessFromDistance(
                        ctx.routingDistance(it.attraction.position.coordinates),
                        attractionsPositions(ctx).map { p -> ctx.routingDistance(p.position.coordinates) }
                    ) * queueTimeScore(queueTime(it.visitors.sumOf { v -> v.cardinality }, it.attraction.capacity, it.attraction.duration.toDouble()))
                ).considerPreviousDestination(it.attraction.position.coordinates, getPreviousDestination(ctx))
            )
        }.maxByOrNull { it.second }?.first?.position?.coordinates ?: getDestination(ctx)
}
