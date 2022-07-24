package it.unibo.alchemist.loader.export.extractors

import it.unibo.alchemist.loader.export.Extractor
import it.unibo.alchemist.model.implementations.molecules.SimpleMolecule
import it.unibo.alchemist.model.interfaces.Actionable
import it.unibo.alchemist.model.interfaces.Environment
import it.unibo.alchemist.model.interfaces.Time
import microcity.Queues.Queue
import microcity.Utils.Molecules.QUEUES
import microcity.Utils.Molecules.VISITOR

class WaitingTimeExporter(override val columnNames: List<String>) : Extractor<Int> {
    override fun <T> extractData(
        environment: Environment<T, *>,
        reaction: Actionable<T>?,
        time: Time,
        step: Long
    ): Map<String, Int> {
        val visitors = environment.nodes.filter { it.contains(SimpleMolecule(VISITOR)) }
        return visitors.map { "waitingTime@${it.id}" }.zip(
            visitors
                .map { (it.getConcentration(SimpleMolecule(QUEUES)) as List<Queue>).any { q -> q.visitors.any { v -> v.id == it.id } } }
                .map {
                    when (it) {
                        true -> 1
                        else -> 0
                    }
                }
        ).toMap().toSortedMap()
    }
}
