package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Device.get
import microcity.Device.getCoordinates
import microcity.Device.has
import microcity.Device.put
import microcity.Positions.Position
import microcity.Utils.Molecules.DESTINATION
import microcity.Utils.Molecules.GUEST
import microcity.Utils.Molecules.QUEUE
import microcity.Utils.Molecules.ROUND_CAPACITY
import microcity.Utils.Molecules.SATISFACTION
import microcity.Utils.Molecules.SATISFACTIONS
import microcity.Utils.Molecules.SATISFIED
import org.protelis.lang.datatype.Tuple

object Utils {

    fun role(ctx: AlchemistExecutionContext<*>, role: String): Boolean =
        has(ctx, role) && (get(ctx, role) as Boolean)

    object Molecules {
        const val ACTIVITY: String = "activity"
        const val GUEST: String = "guest"
        const val SATISFIED: String = "satisfied"
        const val ROUND_CAPACITY: String = "roundCapacity"
        const val POSITIONS: String = "org:protelis:microcity:positions"
        const val DESTINATION: String = "org:protelis:microcity:destination"
        const val QUEUE: String = "org:protelis:microcity:queue"
        const val SATISFACTION: String = "org:protelis:microcity:satisfaction"
        const val SATISFACTIONS: String = "satisfactions"
    }

    object Guests {

        @JvmStatic
        fun isGuest(ctx: AlchemistExecutionContext<*>): Boolean =
            role(ctx, GUEST)

        @JvmStatic
        fun satisfy(ctx: AlchemistExecutionContext<*>, value: Boolean) {
            if (isGuest(ctx)) put(ctx, SATISFIED, value)
        }

        @JvmStatic
        fun isSatisfied(ctx: AlchemistExecutionContext<*>): Boolean =
            has(ctx, SATISFIED) && (get(ctx, SATISFIED) as Boolean)

        fun getDestination(ctx: AlchemistExecutionContext<*>): Tuple =
            if (has(ctx, DESTINATION)) get(ctx, DESTINATION) as Tuple else getCoordinates(ctx)

        fun getSatisfactions(ctx: AlchemistExecutionContext<*>): Double = when {
            has(ctx, SATISFACTIONS) -> get(ctx, SATISFACTIONS) as Double
            else -> 0.0
        }

    }

    object Activities {
        fun getQueue(ctx: AlchemistExecutionContext<*>): List<Position> = when {
            has(ctx, QUEUE) -> get(ctx, QUEUE) as List<Position>
            else -> arrayListOf()
        }

        fun getSatisfied(ctx: AlchemistExecutionContext<*>): List<Position> = when {
            has(ctx, SATISFACTION) -> get(ctx, SATISFACTION) as List<Position>
            else -> arrayListOf()
        }

        fun getGuestsPerRound(ctx: AlchemistExecutionContext<*>): Int =
            (get(ctx, ROUND_CAPACITY) as Double).toInt()
    }
}