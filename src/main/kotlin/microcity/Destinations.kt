package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Device.getCoordinates
import microcity.Positions.getPositions
import microcity.Utils.Guests.isSatisfied
import microcity.Utils.Guests.satisfy
import org.protelis.lang.datatype.Tuple
import kotlin.random.Random

object Destinations {

    @JvmStatic
    fun getNext(ctx: AlchemistExecutionContext<*>, current: Tuple): Tuple {
        val positions = getPositions(ctx)
        return when {
            (isSatisfied(ctx) && positions.isNotEmpty()) -> {
                val next = Random.nextInt(0, positions.size)
                val position = positions.find { it.id == next }?.coordinates ?: getCoordinates(ctx)
                satisfy(ctx, position == getCoordinates(ctx))
                position
            }
            else -> current
        }
    }

}