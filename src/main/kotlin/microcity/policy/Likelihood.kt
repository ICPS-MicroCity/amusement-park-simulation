package microcity.policy

import kotlin.math.exp

fun likelihood(popularity: Double, laziness: Double): Double = popularity / laziness

fun lazinessFromDistance(distance: Double, distances: List<Double>): Double =
    sigmoid(normalize(distance, distances.min(), distances.max()))

fun sigmoid(x: Double): Double = 1 / (1 + exp(-x + 3))

fun normalize(x: Double, min: Double, max: Double): Double = (x - min) / (max - min) * 5
