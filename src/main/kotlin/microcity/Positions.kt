package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Devices.get
import microcity.Devices.getCoordinates
import microcity.Devices.getId
import microcity.Devices.has
import microcity.Devices.put
import microcity.Utils.Molecules.MOVING
import microcity.Utils.Molecules.POSITIONS
import microcity.Utils.Visitors.setPreviousDestination
import microcity.entities.Entities.Attraction
import microcity.entities.Entities.attraction
import org.protelis.lang.datatype.Tuple

object Positions {
    data class Position(val id: Int, val coordinates: Tuple)

    /**
     * Update the isMoving molecule.
     */
    @JvmStatic
    fun updateMoving(ctx: AlchemistExecutionContext<*>, position: Tuple) {
        put(ctx, MOVING, position != getCoordinates(ctx))
        if (position == getCoordinates(ctx)) {
            setPreviousDestination(ctx, position)
        }
    }

    /**
     * Creates a list containing the position of the current node.
     */
    @JvmStatic
    fun createPositions(ctx: AlchemistExecutionContext<*>): List<Position> =
        listOf(Position(getId(ctx), getCoordinates(ctx)))

    /**
     * Gets the content of the POSITIONS molecule, which contains the list of all the positions of the attractions.
     */
    fun attractionsPositions(ctx: AlchemistExecutionContext<*>): List<Attraction> = when {
        has(ctx, POSITIONS) -> when (val positions = get(ctx, POSITIONS)) {
            is Tuple -> positions.tupleToList()
            is List<*> -> positions.map { it as Attraction }
            else -> emptyList()
        }
        else -> attraction(ctx)
    }

    private fun Tuple.tupleToList(): List<Attraction> =
        this.iterator().asSequence().map { it as Attraction }.toList()
}
