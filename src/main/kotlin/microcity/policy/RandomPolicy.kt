package microcity.policy

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Positions
import org.protelis.lang.datatype.Tuple
import kotlin.random.Random

class RandomPolicy : NextPolicy {
    override fun getNext(ctx: AlchemistExecutionContext<*>): Tuple = Positions.getPositions(ctx)[Random.nextInt(0, Positions.getPositions(ctx).size)].coordinates
}
