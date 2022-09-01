package microcity.policy

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Positions.getPositions
import microcity.Utils.Visitors.getDestination
import org.protelis.lang.datatype.Tuple

class LazyPopularPolicy : NextPolicy {
    override fun getNext(ctx: AlchemistExecutionContext<*>): Tuple =
        getPositions(ctx)
            .filter { it.position.coordinates != getDestination(ctx) }
            .map {
                Pair(
                    it.position.coordinates,
                    likelihood(
                        it.popularity.toDouble(),
                        lazinessFromDistance(
                            ctx.routingDistance(it.position.coordinates),
                            getPositions(ctx)
                                .filter { p -> p.position.coordinates != getDestination(ctx) }
                                .map { p -> ctx.routingDistance(p.position.coordinates) }
                        )
                    )
                )
            }
            .maxBy { it.second }.first
}
