package microcity

import java.util.Random
import kotlin.math.exp

object Maths {
    fun sigmoid(x: Double): Double = 1 / (1 + exp(-x + 3))

    fun normalize(x: Double, min: Double, max: Double): Double = (x - min) / (max - min)

    fun gaussian(mean: Double = 3.5, stdDev: Double = 1.0): Double = Random().nextGaussian(mean, stdDev)
}
