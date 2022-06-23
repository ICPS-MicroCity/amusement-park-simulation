package microcity

import org.protelis.lang.datatype.Tuple
import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Utils.role
import microcity.Device.has
import microcity.Device.get
import microcity.Device.getId
import microcity.Device.getCoordinates
import microcity.Utils.Molecules.POSITIONS
import microcity.Utils.Molecules.ACTIVITY

object Positions {
    data class Position(val id: Int, val coordinates: Tuple?)

    private fun Tuple.toList(): List<Position> {
        val array = this.toArray()
        println(array.toString())
        return ArrayList<Position>(array.toList().map {it as Position})
    }

    @JvmStatic
    fun positionFrom(id: Int, coordinates: Tuple): Position =
        Position(id, coordinates)

    @JvmStatic
    fun createPositions(ctx: AlchemistExecutionContext<*>): List<Position> = when {
        role(ctx, ACTIVITY) -> ArrayList(listOf(Position(getId(ctx), getCoordinates(ctx))))
        else -> ArrayList()
    }

    @JvmStatic
    fun getPositions(ctx: AlchemistExecutionContext<*>): List<Position> = when {
        has(ctx, POSITIONS) -> (get(ctx, POSITIONS) as Tuple).toList()
        else -> createPositions(ctx)
    }

    @JvmStatic
    fun union(l1: List<Position>, l2: List<Position>): List<Position> =
        ArrayList(l1.union(l2).toList()
            .filter { it.id >= 0 }
            .sortedBy { it.id })



}