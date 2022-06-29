package microcity.export

import it.unibo.alchemist.loader.export.Extractor
import it.unibo.alchemist.model.interfaces.Environment
import it.unibo.alchemist.model.interfaces.Reaction
import it.unibo.alchemist.model.interfaces.Time

class MicroCityExporter : Extractor<Double> {
    val numbers: List<Double> = listOf(0.0, 1.0, 2.0)

    override val columnNames: List<String>
        get() = listOf("org:protelis:microcity:satisfaction")

    override fun <T> extractData(
        environment: Environment<T, *>,
        reaction: Reaction<T>?,
        time: Time,
        step: Long
    ): Map<String, Double> {
        return columnNames.flatMap { name ->
            numbers.map { number ->
                name to number
            }
        }.toMap()
    }
}

// Watch this to understand how to do this
// https://github.com/DanySK/Experiment-2022-Coordination-Space-Fluid/blob/master/src/main/kotlin/it/unibo/alchemist/loader/export/extractors/CoordDataEport.kt