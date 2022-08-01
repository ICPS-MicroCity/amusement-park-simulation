package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Device.get
import microcity.Device.getCoordinates
import microcity.Device.getId
import microcity.Device.has
import microcity.Device.put
import microcity.Utils.Molecules.ATTRACTION
import microcity.Utils.Molecules.POSITIONS
import microcity.Utils.role
import org.protelis.lang.datatype.Tuple

object Positions {
    data class Position(val id: Int, val coordinates: Tuple)

    private fun Tuple.tupleToList(): List<Position> =
        this.iterator().asSequence().map { it as Position }.toList()

    @JvmStatic
    fun updateMoving(ctx: AlchemistExecutionContext<*>, position: Tuple) {
        put(ctx, "moving", position != getCoordinates(ctx))
    }

    @JvmStatic
    fun createPositions(ctx: AlchemistExecutionContext<*>): List<Position> =
        arrayListOf(Position(getId(ctx), getCoordinates(ctx)))

    @JvmStatic
    fun attractionPositions(ctx: AlchemistExecutionContext<*>): List<Position> = when {
        role(ctx, ATTRACTION) -> arrayListOf(Position(getId(ctx), getCoordinates(ctx)))
        else -> ArrayList()
    }

    @JvmStatic
    fun attractionUnion(l1: List<Position>, l2: List<Position>): List<Position> =
        ArrayList(l1.union(l2).toList().sortedBy { it.id })

    fun getPositions(ctx: AlchemistExecutionContext<*>): List<Position> = when {
        has(ctx, POSITIONS) -> when (val positions = get(ctx, POSITIONS)) {
            is Tuple -> positions.tupleToList()
            is java.util.ArrayList<*> -> positions.map { it as Position }
            else -> {
                println(positions.javaClass)
                ArrayList()
            }
        }
        else -> attractionPositions(ctx)
    }
}
