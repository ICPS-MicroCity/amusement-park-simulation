package microcity.policy

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Positions.getPositions
import org.protelis.lang.datatype.Tuple
import kotlin.math.exp

class LazyPopularPolicy : NextPolicy {
    override fun getNext(ctx: AlchemistExecutionContext<*>): Tuple =
        getPositions(ctx)
            .map {
                Pair(
                    it.coordinates,
                    likelihood(
                        it.popularity.toDouble(),
                        lazinessFromDistance(
                            ctx.routingDistance(it.coordinates),
                            getPositions(ctx).map { p -> ctx.routingDistance(p.coordinates) }
                        )
                    )
                )
            }
            .maxBy { it.second }.first

    private fun likelihood(popularity: Double, laziness: Double): Double = popularity / laziness

    private fun lazinessFromDistance(distance: Double, distances: List<Double>): Double =
        sigmoid(normalize(distance, distances.min(), distances.max()))

    private fun sigmoid(x: Double): Double = 1 / (1 + exp(-x + 3))

    private fun normalize(x: Double, min: Double, max: Double): Double = (x - min) / (max - min) * 5
}
