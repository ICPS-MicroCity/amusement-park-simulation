package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Device.get
import microcity.Device.has
import microcity.Device.put
import microcity.Utils.Molecules.SATISFIED

object Utils {

    fun role(ctx: AlchemistExecutionContext<*>, role: String): Boolean =
        has(ctx, role) && (get(ctx, role) as Boolean)

    object Molecules {
        const val POSITIONS: String = "org:protelis:microcity:positions"
        const val ACTIVITY: String = "activity"
        const val GUEST: String = "guest"
        const val SATISFIED: String = "satisfied"
        const val DESTINATION: String = "org:protelis:microcity:destination"
    }

    object Guests {

        @JvmStatic
        fun satisfy(ctx: AlchemistExecutionContext<*>, value: Boolean) {
            put(ctx, SATISFIED, value)
        }

        fun isSatisfied(ctx: AlchemistExecutionContext<*>): Boolean =
                has(ctx, SATISFIED) && (get(ctx, SATISFIED) as Boolean)
    }
}