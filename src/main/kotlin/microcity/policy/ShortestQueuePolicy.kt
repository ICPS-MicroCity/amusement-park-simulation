package microcity.policy

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Positions
import microcity.Utils
import org.protelis.lang.datatype.Tuple
import kotlin.random.Random

class ShortestQueuePolicy : NextPolicy {
    override fun getNext(ctx: AlchemistExecutionContext<*>, current: Tuple): Tuple = when {
        Utils.Visitors.isSatisfied(ctx) && Positions.getPositions(ctx).isNotEmpty() ->
            Positions.getPositions(ctx)[Random.nextInt(0, Positions.getPositions(ctx).size)].coordinates
        else -> current
    }
}
