package microcity.policy

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Positions
import org.protelis.lang.datatype.Tuple
import kotlin.random.Random

/**
 * A policy that chooses the next destination randomly.
 */
class RandomPolicy : NextPolicy {
    override fun getNext(ctx: AlchemistExecutionContext<*>): Tuple =
        Positions.attractionsPositions(ctx)[Random.nextInt(0, Positions.attractionsPositions(ctx).size)].position.coordinates
}
