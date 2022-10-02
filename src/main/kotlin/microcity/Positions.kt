package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Device.get
import microcity.Device.getCoordinates
import microcity.Device.getId
import microcity.Device.has
import microcity.Device.put
import microcity.Utils.Attractions.getCapacity
import microcity.Utils.Attractions.getDuration
import microcity.Utils.Attractions.getPopularity
import microcity.Utils.Molecules.ATTRACTION
import microcity.Utils.Molecules.MOVING
import microcity.Utils.Molecules.POSITIONS
import microcity.Utils.Visitors.setPreviousDestination
import microcity.Utils.role
import org.protelis.lang.datatype.Tuple

object Positions {
    data class Position(val id: Int, val coordinates: Tuple)
    data class Attraction(val position: Position, val popularity: Int, val capacity: Int, val duration: Int)

    private fun Tuple.tupleToList(): List<Attraction> =
        this.iterator().asSequence().map { it as Attraction }.toList()

    @JvmStatic
    fun updateMoving(ctx: AlchemistExecutionContext<*>, position: Tuple) {
        put(ctx, MOVING, position != getCoordinates(ctx))
        if (position == getCoordinates(ctx)) {
            setPreviousDestination(ctx, position)
        }
    }

    @JvmStatic
    fun createPositions(ctx: AlchemistExecutionContext<*>): List<Position> =
        listOf(Position(getId(ctx), getCoordinates(ctx)))

    @JvmStatic
    fun attractionPositions(ctx: AlchemistExecutionContext<*>): List<Attraction> = when {
        role(ctx, ATTRACTION) -> listOf(Attraction(Position(getId(ctx), getCoordinates(ctx)), getPopularity(ctx), getCapacity(ctx), getDuration(ctx)))
        else -> emptyList()
    }

    @JvmStatic
    fun attractionUnion(l1: List<Attraction>, l2: List<Attraction>): List<Attraction> =
        l1.union(l2).toList().sortedBy { it.position.id }

    fun getPositions(ctx: AlchemistExecutionContext<*>): List<Attraction> = when {
        has(ctx, POSITIONS) -> when (val positions = get(ctx, POSITIONS)) {
            is Tuple -> positions.tupleToList()
            is List<*> -> positions.map { it as Attraction }
            else -> emptyList()
        }
        else -> attractionPositions(ctx)
    }
}
