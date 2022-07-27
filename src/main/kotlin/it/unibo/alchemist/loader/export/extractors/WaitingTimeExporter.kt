package it.unibo.alchemist.loader.export.extractors

import it.unibo.alchemist.loader.export.Extractor
import it.unibo.alchemist.model.implementations.molecules.SimpleMolecule
import it.unibo.alchemist.model.interfaces.Actionable
import it.unibo.alchemist.model.interfaces.Environment
import it.unibo.alchemist.model.interfaces.Time
import microcity.Utils.Molecules.VISITOR
import microcity.Utils.Molecules.WAITING_TIME

class WaitingTimeExporter(override val columnNames: List<String> = listOf("waitingTime@id")) :
    Extractor<Double> {
    override fun <T> extractData(
        environment: Environment<T, *>,
        reaction: Actionable<T>?,
        time: Time,
        step: Long
    ): Map<String, Double> {
        val visitors = environment.nodes.filter { it.contains(SimpleMolecule(VISITOR)) }
        return visitors.map { "waitingTime@${it.id}" }.zip(
            visitors
                .map { it.getConcentration(SimpleMolecule(WAITING_TIME)) as Double }
        ).toMap().toSortedMap()
    }
}
