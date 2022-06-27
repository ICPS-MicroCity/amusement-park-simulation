package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Device.get
import microcity.Device.getCoordinates
import microcity.Device.has
import microcity.Device.put
import microcity.Positions.Position
import microcity.Utils.Molecules.DESTINATION
import microcity.Utils.Molecules.ENQUEUED
import microcity.Utils.Molecules.GUEST
import microcity.Utils.Molecules.QUEUE
import microcity.Utils.Molecules.ROUND_CAPACITY
import microcity.Utils.Molecules.SATISFIED
import org.protelis.lang.datatype.Tuple

object Utils {

    fun role(ctx: AlchemistExecutionContext<*>, role: String): Boolean =
        has(ctx, role) && (get(ctx, role) as Boolean)

    @JvmStatic
    fun debug(toDebug: Any) {
        println(toDebug)
    }

    object Molecules {
        const val POSITIONS: String = "org:protelis:microcity:positions"
        const val ACTIVITY: String = "activity"
        const val GUEST: String = "guest"
        const val SATISFIED: String = "satisfied"
        const val ENQUEUED: String = "enqueued"
        const val DESTINATION: String = "org:protelis:microcity:destination"
        const val QUEUE: String = "org:protelis:microcity:queue"
        const val ROUND_CAPACITY: String = "roundCapacity"
    }

    object Guests {

        @JvmStatic
        fun isGuest(ctx: AlchemistExecutionContext<*>): Boolean =
            has(ctx, GUEST) && (get(ctx, GUEST) as Boolean)

        fun getDestination(ctx: AlchemistExecutionContext<*>): Tuple =
            if (has(ctx, DESTINATION)) get(ctx, DESTINATION) as Tuple else getCoordinates(ctx)

        @JvmStatic
        fun satisfy(ctx: AlchemistExecutionContext<*>, value: Boolean) {
            put(ctx, SATISFIED, value)
        }

        fun enqueue(ctx: AlchemistExecutionContext<*>, value: Boolean) {
            put(ctx, ENQUEUED, value)
        }

        @JvmStatic
        fun isEnqueued(ctx: AlchemistExecutionContext<*>): Boolean =
            has(ctx, ENQUEUED) && (get(ctx, ENQUEUED) as Boolean)

        @JvmStatic
        fun isSatisfied(ctx: AlchemistExecutionContext<*>): Boolean =
            has(ctx, SATISFIED) && (get(ctx, SATISFIED) as Boolean)

    }

    object Activities {
        @JvmStatic
        fun getQueue(ctx: AlchemistExecutionContext<*>): List<Position> = when {
            has(ctx, QUEUE) -> get(ctx, QUEUE) as List<Position>
            else -> arrayListOf()
        }

        @JvmStatic
        fun updateQueue(ctx: AlchemistExecutionContext<*>, queue: List<Position>) {
            put(ctx, QUEUE, queue)
        }

        @JvmStatic
        fun getGuestsPerRound(ctx: AlchemistExecutionContext<*>): Int =
            (get(ctx, ROUND_CAPACITY) as Double).toInt()
    }
}