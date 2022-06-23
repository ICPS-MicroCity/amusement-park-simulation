package microcity

import org.protelis.lang.datatype.Tuple
import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Utils.role
import microcity.Device.has
import microcity.Device.get
import microcity.Device.getId
import microcity.Device.getCoordinates

object Positions {
    data class Position(val id: Int, val coordinates: Tuple?)

    @JvmStatic
    fun positionFrom(id: Int, coordinates: Tuple): Position =
        Position(id, coordinates)

    @JvmStatic
    fun createPositions(ctx: AlchemistExecutionContext<*>): List<Position> = when {
        role(ctx, "activity") -> ArrayList(listOf(Position(getId(ctx), getCoordinates(ctx))))
        else -> ArrayList()
    }

    @JvmStatic
    fun getPositions(ctx: AlchemistExecutionContext<*>): List<Position> = when {
        has(ctx, "positions") -> get(ctx, "positions") as List<Position>
        else -> createPositions(ctx, getId(ctx))
    }

    @JvmStatic
    fun union(l1: List<Position>, l2: List<Position>): List<Position> =
        ArrayList(l1.union(l2).toList()
            .filter { it.id >= 0 }
            .sortedBy { it.id })



}