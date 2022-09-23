package microcity.policy

import microcity.Maths.normalize
import microcity.Maths.sigmoid
import org.protelis.lang.datatype.Tuple

fun likelihood(popularity: Double, laziness: Double): Double = popularity / laziness

fun lazinessFromDistance(distance: Double, distances: List<Double>): Double =
    sigmoid(normalize(distance, distances.min(), distances.max()) * 5)

fun queueTime(queueSize: Int, capacity: Int, duration: Double): Double = (queueSize / capacity) * duration

fun queueTimeScore(queueTime: Double): Int = when {
    queueTime < 300 -> 1 // 5 minutes
    queueTime < 1200 -> 2 // 20 minutes
    queueTime < 1800 -> 3 // 30 minutes
    queueTime < 2700 -> 4 // 45 minutes
    queueTime < 3600 -> 5 // 60 minutes
    else -> 6
}

fun Double.considerPreviousDestination(position: Tuple, previousDestination: Tuple): Double = when (position) {
    previousDestination -> this * 0.5
    else -> this
}
