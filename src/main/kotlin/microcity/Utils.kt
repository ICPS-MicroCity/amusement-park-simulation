package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import org.protelis.lang.datatype.Tuple
import microcity.Device.has
import microcity.Device.get
import microcity.Device.put

object Utils {

    fun role(ctx: AlchemistExecutionContext<*>, role: String): Boolean =
        has(ctx, role) && (get(ctx, role) as Boolean)

    object Molecules {
        val POSITIONS: String = "org:protelis:microcity:positions"
        val ACTIVITY : String = "activity"
    }

    object Guests {

        fun satisfy(ctx: AlchemistExecutionContext<*>, value: Boolean) {
            put(ctx, "satisfied", value)
        }

        fun isSatisfied(ctx: AlchemistExecutionContext<*>): Boolean =
                has(ctx, "satisfied") && (get(ctx, "satisfied") as Boolean)
    }
}