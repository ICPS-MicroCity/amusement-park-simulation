package microcity.policy

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Positions.getPositions
import microcity.Utils.Visitors.getDestination
import org.protelis.lang.datatype.Tuple

class LazyPopularPolicy : NextPolicy {
    override fun getNext(ctx: AlchemistExecutionContext<*>): Tuple =
        getPositions(ctx)
            .filter { it.coordinates != getDestination(ctx) }
            .map {
                Pair(
                    it.coordinates,
                    likelihood(
                        it.popularity.toDouble(),
                        lazinessFromDistance(
                            ctx.routingDistance(it.coordinates),
                            getPositions(ctx)
                                .filter { p -> p.coordinates != getDestination(ctx) }
                                .map { p -> ctx.routingDistance(p.coordinates) }
                        )
                    )
                )
            }
            .maxBy { it.second }.first
}
