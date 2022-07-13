package it.unibo.alchemist.loader.export.extractors

import it.unibo.alchemist.loader.export.Extractor
import it.unibo.alchemist.model.implementations.molecules.SimpleMolecule
import it.unibo.alchemist.model.interfaces.Actionable
import it.unibo.alchemist.model.interfaces.Environment
import it.unibo.alchemist.model.interfaces.Time

class MicroCityExporter : Extractor<Int> {

    override val columnNames: List<String>
        get() = listOf("$SATISFACTIONS@id")

    override fun <T> extractData(
        environment: Environment<T, *>,
        reaction: Actionable<T>?,
        time: Time,
        step: Long
    ): Map<String, Int> {
        val visitors = environment.nodes.filter { it.contains(SimpleMolecule(VISITOR)) }
        return visitors.map { "$SATISFACTIONS@${it.id}" }.zip(
            visitors
                .map { it.getConcentration(SimpleMolecule(SATISFACTIONS))?.asNumber<Int>() }
                .mapNotNull { it ?: 0 }
        ).toMap().toSortedMap()
    }

    companion object {
        private const val VISITOR = "visitor"
        private const val SATISFACTIONS = "satisfactions"

        private inline fun <reified T : Number> Any.asNumber(): T = when (this) {
            is T -> this
            is Number -> when (T::class) {
                Int::class -> toInt()
                Double::class -> toDouble()
                else -> TODO()
            } as T
            else -> TODO()
        }
    }
}
