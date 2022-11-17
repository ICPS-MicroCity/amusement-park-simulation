package microcity.policy

import microcity.Maths.normalize
import microcity.Maths.sigmoid
import org.protelis.lang.datatype.Tuple

/**
 * Calculated the likelihood of a destination based on its popularity and laziness.
 */
fun likelihood(popularity: Double, laziness: Double): Double = popularity / laziness

/**
 * Calculates the laziness of a destination based on its distance with a sigmoid function.
 */
fun lazinessFromDistance(distance: Double, distances: List<Double>): Double =
    sigmoid(normalize(distance, distances.minOrNull() ?: 0.0, distances.maxOrNull() ?: 100.0) * 5)

/**
 * Calculates the time to be spent in a queue based on the capacity of the attraction.
 */
fun queueTime(queueSize: Int, capacity: Int, duration: Double): Double = (queueSize / capacity) * duration

/**
 * Assign a score to a queue time.
 */
fun queueTimeScore(queueTime: Double): Int = when {
    queueTime < 300 -> 1 // 5 minutes
    queueTime < 1200 -> 2 // 20 minutes
    queueTime < 1800 -> 3 // 30 minutes
    queueTime < 2700 -> 4 // 45 minutes
    queueTime < 3600 -> 5 // 60 minutes
    else -> 6
}

/**
 * Makes the last destination less likely to be chosen.
 */
fun Double.considerPreviousDestination(position: Tuple, previousDestination: Tuple): Double = when (position) {
    previousDestination -> this * 0.5
    else -> this
}
