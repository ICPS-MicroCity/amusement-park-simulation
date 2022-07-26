package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Device.get
import microcity.Device.getCoordinates
import microcity.Device.has
import microcity.Device.put
import microcity.Positions.Position
import microcity.Queues.Queue
import microcity.Utils.Molecules.CAPACITY
import microcity.Utils.Molecules.DESTINATION
import microcity.Utils.Molecules.NEXT_POLICY
import microcity.Utils.Molecules.QUEUE
import microcity.Utils.Molecules.QUEUES
import microcity.Utils.Molecules.SATISFACTION
import microcity.Utils.Molecules.SATISFACTIONS
import microcity.Utils.Molecules.SATISFIED
import microcity.Utils.Molecules.SHORTEST_QUEUE_POLICY
import microcity.Utils.Molecules.SHORTEST_QUEUE_RANGE_POLICY
import microcity.Utils.Molecules.VISITOR
import microcity.policy.NextPolicy
import microcity.policy.RandomPolicy
import microcity.policy.ShortestQueueInRangePolicy
import microcity.policy.ShortestQueuePolicy
import org.protelis.lang.datatype.Tuple

object Utils {

    fun role(ctx: AlchemistExecutionContext<*>, role: String): Boolean =
        has(ctx, role) && (get(ctx, role) as Boolean)

    object Molecules {
        const val ATTRACTION: String = "attraction"
        const val VISITOR: String = "visitor"
        const val SATISFIED: String = "satisfied"
        const val CAPACITY: String = "capacity"
        const val POSITIONS: String = "org:protelis:microcity:positions"
        const val DESTINATION: String = "org:protelis:microcity:destination"
        const val QUEUE: String = "org:protelis:microcity:queue"
        const val QUEUES: String = "org:protelis:microcity:queues"
        const val SATISFACTION: String = "org:protelis:microcity:satisfaction"
        const val SATISFACTIONS: String = "satisfactions"
        const val NEXT_POLICY: String = "next-policy"
        const val SHORTEST_QUEUE_POLICY: String = "shortestQueue"
        const val SHORTEST_QUEUE_RANGE_POLICY: String = "shortestQueueRange"
    }

    object Visitors {

        @JvmStatic
        fun isVisitor(ctx: AlchemistExecutionContext<*>): Boolean =
            role(ctx, VISITOR)

        @JvmStatic
        fun satisfy(ctx: AlchemistExecutionContext<*>, value: Boolean) {
            if (isVisitor(ctx)) put(ctx, SATISFIED, value)
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

        fun getQueues(ctx: AlchemistExecutionContext<*>): List<Queue> = when {
            has(ctx, QUEUES) -> when (val l = get(ctx, QUEUES)) {
                is List<*> -> l as List<Queue>
                else -> listOf()
            }
            else -> arrayListOf()
        }

        fun getNextPolicy(ctx: AlchemistExecutionContext<*>): NextPolicy = when {
            has(ctx, NEXT_POLICY) && get(ctx, NEXT_POLICY) == SHORTEST_QUEUE_POLICY -> ShortestQueuePolicy()
            has(ctx, NEXT_POLICY) && get(ctx, NEXT_POLICY) == SHORTEST_QUEUE_RANGE_POLICY -> ShortestQueueInRangePolicy()
            else -> RandomPolicy()
        }
    }

    object Attractions {
        fun getQueue(ctx: AlchemistExecutionContext<*>): List<Position> = when {
            has(ctx, QUEUE) -> get(ctx, QUEUE) as List<Position>
            else -> arrayListOf()
        }

        fun getSatisfied(ctx: AlchemistExecutionContext<*>): List<Position> = when {
            has(ctx, SATISFACTION) -> get(ctx, SATISFACTION) as List<Position>
            else -> arrayListOf()
        }

        fun getVisitorsPerRound(ctx: AlchemistExecutionContext<*>): Int =
            (get(ctx, CAPACITY) as Double).toInt()
    }
}
