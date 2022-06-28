package microcity.export

import it.unibo.alchemist.loader.export.Extractor
import it.unibo.alchemist.model.interfaces.Environment
import it.unibo.alchemist.model.interfaces.Reaction
import it.unibo.alchemist.model.interfaces.Time

class MicroCityExporter : Extractor<Double> {
    override val columnNames: List<String>
        get() = TODO("Not yet implemented")

    override fun <T> extractData(
        environment: Environment<T, *>,
        reaction: Reaction<T>?,
        time: Time,
        step: Long
    ): Map<String, Double> {
        TODO("Not yet implemented")
    }
}

// Watch this to understand how to do this
// https://github.com/DanySK/Experiment-2022-Coordination-Space-Fluid/blob/master/src/main/kotlin/it/unibo/alchemist/loader/export/extractors/CoordDataEport.kt