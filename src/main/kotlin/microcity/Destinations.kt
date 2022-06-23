package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import org.protelis.lang.datatype.Tuple
import microcity.Positions.Position
import microcity.Positions.getPositions
import microcity.Device.get
import microcity.Device.getId
import microcity.Device.getCoordinates
import microcity.Device.has
import microcity.Utils.Guests.isSatisfied
import kotlin.random.Random

object Destinations {

    @JvmStatic
    fun getNext(ctx: AlchemistExecutionContext<*>): Tuple = when {
        isSatisfied(ctx) -> {
            val positions = getPositions(ctx)
            val next = Random.nextInt(0, positions.size)
            satisfy(false)
            return positions.find { it.id == next }?.coordinates ?: getCoordinates()
        }
        else -> {

        }
    }


}