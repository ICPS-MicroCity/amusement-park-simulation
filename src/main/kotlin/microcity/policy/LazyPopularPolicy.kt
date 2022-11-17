package microcity.policy

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Positions.attractionsPositions
import microcity.Utils.Visitors.getDestination
import org.protelis.lang.datatype.Tuple
import kotlin.random.Random

/**
 * A policy that chooses the next destination based on the popularity of the attraction
 * and its distance from the current position.
 */
class LazyPopularPolicy : NextPolicy {
    override fun getNext(ctx: AlchemistExecutionContext<*>): Tuple =
        attractionsPositions(ctx)
            .filter { it.position.coordinates != getDestination(ctx) }
            .map {
                Pair(
                    it.position.coordinates,
                    likelihood(
                        it.popularity.toDouble() * Random.nextInt(1, 20),
                        lazinessFromDistance(
                            ctx.routingDistance(it.position.coordinates),
                            attractionsPositions(ctx)
                                .filter { p -> p.position.coordinates != getDestination(ctx) }
                                .map { p -> ctx.routingDistance(p.position.coordinates) }
                        )
                    )
                )
            }
            .maxByOrNull { it.second }?.first ?: getDestination(ctx)
}
