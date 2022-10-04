package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import microcity.Device.get
import microcity.Device.getCoordinates
import microcity.Device.has
import microcity.Device.put
import microcity.Maths.gaussian
import microcity.Queues.Queue
import microcity.Queues.Visitor
import microcity.Utils.Molecules.CAPACITY
import microcity.Utils.Molecules.CARDINALITY
import microcity.Utils.Molecules.DEQUEUE
import microcity.Utils.Molecules.DESTINATION
import microcity.Utils.Molecules.JUST_DEQUEUED
import microcity.Utils.Molecules.LAZY_POPULAR_POLICY
import microcity.Utils.Molecules.LEADER
import microcity.Utils.Molecules.MOVING
import microcity.Utils.Molecules.NEXT_POLICY
import microcity.Utils.Molecules.POPULARITY
import microcity.Utils.Molecules.PREVIOUS_DESTINATION
import microcity.Utils.Molecules.QUEUE
import microcity.Utils.Molecules.QUEUES
import microcity.Utils.Molecules.RECOMMENDATION_POLICY
import microcity.Utils.Molecules.SATISFACTION
import microcity.Utils.Molecules.SATISFACTIONS
import microcity.Utils.Molecules.SATISFACTION_FREQUENCY
import microcity.Utils.Molecules.SATISFIED
import microcity.Utils.Molecules.SHORTEST_QUEUE_POLICY
import microcity.Utils.Molecules.SHORTEST_QUEUE_RANGE_POLICY
import microcity.Utils.Molecules.SITUATED_RECOMMENDATION_POLICY
import microcity.Utils.Molecules.VISITOR
import microcity.policy.*
import org.protelis.lang.datatype.Tuple

object Utils {

    fun role(ctx: AlchemistExecutionContext<*>, role: String): Boolean =
        has(ctx, role) && (get(ctx, role) as Boolean)

    @JvmStatic
    fun leader(ctx: AlchemistExecutionContext<*>): Boolean =
        has(ctx, LEADER) && (get(ctx, LEADER) as Boolean)

    object Molecules {
        const val LEADER: String = "leader"
        const val ATTRACTION: String = "attraction"
        const val VISITOR: String = "visitor"
        const val SATISFIED: String = "satisfied"
        const val CAPACITY: String = "capacity"
        const val POSITIONS: String = "org:protelis:microcity:aggregatePositions"
        const val CARDINALITY: String = "org:protelis:microcity:cardinality"
        const val DESTINATION: String = "org:protelis:microcity:destination"
        const val POPULARITY: String = "popularity"
        const val MOVING: String = "moving"
        const val QUEUE: String = "org:protelis:microcity:queue"
        const val QUEUES: String = "org:protelis:microcity:aggregateQueues"
        const val SATISFACTION: String = "org:protelis:microcity:satisfaction"
        const val DEQUEUE: String = "org:protelis:microcity:dequeue"
        const val SATISFACTION_FREQUENCY: String = "satisfaction-frequency"
        const val SATISFACTIONS: String = "satisfactions"
        const val NEXT_POLICY: String = "next-policy"
        const val RECOMMENDATION: String = "org:protelis:microcity:recommendation"
        const val RECOMMENDATION_POLICY: String = "recommendation-policy"
        const val JUST_DEQUEUED: String = "just-dequeued"
        const val SHORTEST_QUEUE_POLICY: String = "shortestQueue"
        const val SHORTEST_QUEUE_RANGE_POLICY: String = "shortestQueueRange"
        const val SITUATED_RECOMMENDATION_POLICY: String = "situatedRecommendation"
        const val LAZY_POPULAR_POLICY: String = "lazyPopular"
        const val PREVIOUS_DESTINATION: String = "previous-destination"
        const val WAITING_TIME: String = "waiting-time"
    }

    object Visitors {

        @JvmStatic
        fun isVisitor(ctx: AlchemistExecutionContext<*>): Boolean =
            role(ctx, VISITOR)

        @JvmStatic
        fun isMoving(ctx: AlchemistExecutionContext<*>): Boolean =
            isVisitor(ctx) && has(ctx, MOVING) && get(ctx, MOVING) as Boolean

        @JvmStatic
        fun satisfy(ctx: AlchemistExecutionContext<*>, value: Boolean) {
            if (isVisitor(ctx)) put(ctx, SATISFIED, value)
        }

        @JvmStatic
        fun checkSatisfaction(ctx: AlchemistExecutionContext<*>, satisfy: Boolean) {
            if (satisfy && !isSatisfied(ctx)) satisfy(ctx, true)
        }

        @JvmStatic
        fun isSatisfied(ctx: AlchemistExecutionContext<*>): Boolean =
            has(ctx, SATISFIED) && (get(ctx, SATISFIED) as Boolean)

        fun setPreviousDestination(ctx: AlchemistExecutionContext<*>, destination: Tuple) {
            put(ctx, PREVIOUS_DESTINATION, destination)
        }

        fun getPreviousDestination(ctx: AlchemistExecutionContext<*>): Tuple =
            if (has(ctx, PREVIOUS_DESTINATION)) get(ctx, PREVIOUS_DESTINATION) as Tuple else getCoordinates(ctx)

        fun getDestination(ctx: AlchemistExecutionContext<*>): Tuple = get(ctx, DESTINATION) as Tuple

        fun getSatisfactions(ctx: AlchemistExecutionContext<*>): Double = when {
            has(ctx, SATISFACTIONS) -> get(ctx, SATISFACTIONS) as Double
            else -> 0.0
        }

        fun getQueues(ctx: AlchemistExecutionContext<*>): List<Queue> = when {
            has(ctx, QUEUES) -> when (val l = get(ctx, QUEUES)) {
                is List<*> -> l as List<Queue>
                else -> listOf()
            }
            else -> listOf()
        }

        fun getNextPolicy(ctx: AlchemistExecutionContext<*>): NextPolicy = when {
            has(ctx, NEXT_POLICY) && get(ctx, NEXT_POLICY) == SHORTEST_QUEUE_POLICY -> ShortestQueuePolicy()
            has(ctx, NEXT_POLICY) && get(ctx, NEXT_POLICY) == SHORTEST_QUEUE_RANGE_POLICY -> ShortestQueueInRangePolicy()
            has(ctx, NEXT_POLICY) && get(ctx, NEXT_POLICY) == LAZY_POPULAR_POLICY -> LazyPopularPolicy()
            else -> RandomPolicy()
        }

        fun getRecommendationPolicy(ctx: AlchemistExecutionContext<*>): NextPolicy = when {
            has(ctx, RECOMMENDATION_POLICY) && get(ctx, RECOMMENDATION_POLICY) == SITUATED_RECOMMENDATION_POLICY -> SituatedRecommendationPolicy()
            else -> getNextPolicy(ctx)
        }

        @JvmStatic
        fun setGroupCardinality(): Int = gaussian().toInt().coerceIn(1, 6)

        fun getCardinality(ctx: AlchemistExecutionContext<*>): Int = when {
            has(ctx, CARDINALITY) -> get(ctx, CARDINALITY) as Int
            else -> 0
        }
    }

    object Attractions {
        fun getQueue(ctx: AlchemistExecutionContext<*>): List<Visitor> = when {
            has(ctx, QUEUE) -> (get(ctx, QUEUE) as List<Visitor>)
            else -> listOf()
        }

        @JvmStatic
        fun getDequeued(ctx: AlchemistExecutionContext<*>): List<Visitor> = when {
            has(ctx, DEQUEUE) -> get(ctx, DEQUEUE) as List<Visitor>
            else -> emptyList()
        }

        fun justDequeued(ctx: AlchemistExecutionContext<*>, visitors: List<Visitor>) {
            put(ctx, JUST_DEQUEUED, visitors)
        }

        fun getJustDequeued(ctx: AlchemistExecutionContext<*>): List<Visitor> = when {
            has(ctx, JUST_DEQUEUED) -> get(ctx, JUST_DEQUEUED) as List<Visitor>
            else -> emptyList()
        }

        @JvmStatic
        fun getSatisfied(ctx: AlchemistExecutionContext<*>): List<Visitor> = when {
            has(ctx, SATISFACTION) -> get(ctx, SATISFACTION) as List<Visitor>
            else -> emptyList()
        }

        fun getCapacity(ctx: AlchemistExecutionContext<*>): Int = (get(ctx, CAPACITY) as Double).toInt()

        @JvmStatic
        fun getPopularity(ctx: AlchemistExecutionContext<*>): Int = (get(ctx, POPULARITY) as Double).toInt()

        @JvmStatic
        fun getDuration(ctx: AlchemistExecutionContext<*>): Int = (1 / (get(ctx, SATISFACTION_FREQUENCY) as Double)).toInt()
    }
}
