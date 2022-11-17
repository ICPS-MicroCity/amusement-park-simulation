package microcity

import it.unibo.alchemist.model.implementations.times.DoubleTime
import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Devices.get
import microcity.Devices.getCoordinates
import microcity.Devices.getId
import microcity.Positions.Position
import microcity.Utils.Attractions.getCapacity
import microcity.Utils.Attractions.getDequeued
import microcity.Utils.Attractions.getDuration
import microcity.Utils.Attractions.getJustDequeued
import microcity.Utils.Attractions.getPopularity
import microcity.Utils.Attractions.getQueue
import microcity.Utils.Attractions.getSatisfied
import microcity.Utils.Attractions.justDequeued
import microcity.Utils.Molecules.ATTRACTION
import microcity.Utils.Molecules.MOVING
import microcity.Utils.Molecules.VISITOR
import microcity.Utils.Molecules.WAITING_TIME
import microcity.Utils.role
import microcity.entities.Entities.Attraction
import microcity.entities.Entities.Visitor
import microcity.entities.Entities.visitor

object Queues {
    data class Queue(val attraction: Attraction, var visitors: List<Visitor>)

    /**
     * Creates a queue for the attraction.
     */
    @JvmStatic
    fun queue(ctx: AlchemistExecutionContext<*>): List<Queue> = when {
        role(ctx, ATTRACTION) -> listOf(
            Queue(
                Attraction(
                    Position(getId(ctx), getCoordinates(ctx)),
                    getPopularity(ctx),
                    getCapacity(ctx),
                    getDuration(ctx)
                ),
                getQueue(ctx)
            )
        )
        else -> emptyList()
    }

    /**
     * Aggregates two fields of aggregate queues.
     */
    @JvmStatic
    fun queuesUnion(a: List<Queue>, b: List<Queue>): List<Queue> =
        a.union(b).toList().sortedBy { it.attraction.position.id }

    /**
     * Dequeues a set of visitors from the queue of the attraction.
     */
    @JvmStatic
    fun dequeue(ctx: AlchemistExecutionContext<*>): List<Visitor> = when {
        role(ctx, ATTRACTION) && getQueue(ctx).isNotEmpty() ->
            getQueue(ctx).mapIndexed { index, visitor ->
                Pair(visitor, getQueue(ctx).take(index + 1).sumOf { v -> v.cardinality })
            }.takeWhile { it.second < getCapacity(ctx) }.map { it.first }.also { justDequeued(ctx, it) }
        else -> emptyList()
    }

    /**
     * Empties the set of just dequeued visitors.
     */
    @JvmStatic
    fun emptyDequeued(ctx: AlchemistExecutionContext<*>) {
        justDequeued(ctx, emptyList())
    }

    /**
     * Aggregates two fields of visitors to create a queue.
     */
    @JvmStatic
    fun visitorUnionForQueue(ctx: AlchemistExecutionContext<*>, a: List<Visitor>, b: List<Visitor>): List<Visitor> = when {
        role(ctx, ATTRACTION) -> b.union(a)
            .filter {
                it.position.coordinates == getCoordinates(ctx) &&
                    it.destination == getCoordinates(ctx) &&
                    !it.satisfied &&
                    it.position.id != getId(ctx) &&
                    !getJustDequeued(ctx).map { p -> p.position.id }.contains(it.position.id) &&
                    !getDequeued(ctx).map { p -> p.position.id }.contains(it.position.id) &&
                    !getSatisfied(ctx).map { p -> p.position.id }.contains(it.position.id)
            }
        else -> visitor(ctx)
    }

    /**
     * Updates the content of the WAITING_TIME molecule.
     */
    @JvmStatic
    fun updateWaitingTime(ctx: AlchemistExecutionContext<*>, time: DoubleTime) {
        if (role(ctx, VISITOR) && !(get(ctx, MOVING) as Boolean)) {
            Devices.put(
                ctx,
                WAITING_TIME,
                DoubleTime(get(ctx, WAITING_TIME) as Double).plus(
                    DoubleTime(ctx.currentTime.toDouble()).minus(time)
                ).toDouble()
            )
        }
    }
}
