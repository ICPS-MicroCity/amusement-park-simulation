package microcity.entities

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Devices
import microcity.Positions.Position
import microcity.Positions.createPositions
import microcity.Utils
import org.protelis.lang.datatype.Tuple

object Entities {
    data class Attraction(val position: Position, val popularity: Int, val capacity: Int, val duration: Int)
    data class Visitor(val position: Position, val destination: Tuple, val cardinality: Int, val satisfied: Boolean)

    /**
     * Creates a list containing the current attraction.
     */
    @JvmStatic
    fun attraction(ctx: AlchemistExecutionContext<*>): List<Attraction> = when {
        Utils.role(ctx, Utils.Molecules.ATTRACTION) -> listOf(
            Attraction(
                Position(
                    Devices.getId(ctx),
                    Devices.getCoordinates(ctx)
                ),
                Utils.Attractions.getPopularity(ctx),
                Utils.Attractions.getCapacity(ctx),
                Utils.Attractions.getDuration(ctx)
            )
        )
        else -> emptyList()
    }

    /**
     * Aggregates two fields of attractions.
     */
    @JvmStatic
    fun attractionUnion(l1: List<Attraction>, l2: List<Attraction>): List<Attraction> =
        l1.union(l2).toList().sortedBy { it.position.id }

    /**
     * Creates a list containing the current visitor.
     */
    @JvmStatic
    fun visitor(ctx: AlchemistExecutionContext<*>): List<Visitor> = when {
        Utils.role(ctx, Utils.Molecules.VISITOR) -> listOf(
            Visitor(
                createPositions(ctx)[0],
                Utils.Visitors.getDestination(ctx),
                Utils.Visitors.getCardinality(ctx),
                Utils.Visitors.isSatisfied(ctx)
            )
        )
        else -> listOf()
    }
}
