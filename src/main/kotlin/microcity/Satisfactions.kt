package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.entities.Entities

object Satisfactions {
    /**
     * Increases the count of satisfactions of the visitor.
     */
    @JvmStatic
    fun addSatisfaction(ctx: AlchemistExecutionContext<*>, value: Boolean) {
        if (value) {
            Devices.put(ctx, Utils.Molecules.SATISFACTIONS, Utils.Visitors.getSatisfactions(ctx) + 1)
        }
    }

    /**
     * Aggregates fields of satisfied visitors.
     * If a visitor finds itself satisfied, it will flag its SATISFIED molecule and then leave the attraction.
     */
    @JvmStatic
    fun satisfactionUnion(ctx: AlchemistExecutionContext<*>, a: List<Entities.Visitor>, b: List<Entities.Visitor>): List<Entities.Visitor> = when {
        Utils.role(ctx, Utils.Molecules.VISITOR) -> a.union(b).filter { it.position.id == Devices.getId(ctx) }.also {
            addSatisfaction(ctx, it.isNotEmpty())
            Utils.Visitors.checkSatisfaction(ctx, it.isNotEmpty())
        }.let { emptyList() }
        else -> a.union(b).filter { it.position.coordinates == Devices.getCoordinates(ctx) && !a.intersect(b.toSet()).contains(it) }.also {
            Devices.put(ctx, Utils.Molecules.DEQUEUE, emptyList<Entities.Visitor>())
        }
    }

    /**
     * If visitor, gets the satisfied visitors.
     * If attraction, gets the dequeued visitors.
     */
    @JvmStatic
    fun justSatisfied(ctx: AlchemistExecutionContext<*>): List<Entities.Visitor> = when {
        Utils.role(ctx, Utils.Molecules.VISITOR) -> Utils.Attractions.getSatisfied(ctx)
        else -> Utils.Attractions.getDequeued(ctx)
    }
}
