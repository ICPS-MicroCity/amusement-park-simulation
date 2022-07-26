package it.unibo.alchemist.loader.export.extractors

import it.unibo.alchemist.loader.export.Extractor
import it.unibo.alchemist.model.implementations.molecules.SimpleMolecule
import it.unibo.alchemist.model.interfaces.Actionable
import it.unibo.alchemist.model.interfaces.Environment
import it.unibo.alchemist.model.interfaces.Time
import microcity.Queues.Queue
import microcity.Utils.Molecules.QUEUES
import microcity.Utils.Molecules.VISITOR
import org.protelis.lang.datatype.Tuple

class WaitingTimeExporter(override val columnNames: List<String> = listOf("waitingTime@id")) :
    Extractor<Int> {
    override fun <T> extractData(
        environment: Environment<T, *>,
        reaction: Actionable<T>?,
        time: Time,
        step: Long
    ): Map<String, Int> {
        val visitors = environment.nodes.filter { it.contains(SimpleMolecule(VISITOR)) }
        return visitors.map { "waitingTime@${it.id}" }.zip(
            visitors
                .map { toList(it.getConcentration(SimpleMolecule(QUEUES))).any { q -> q.visitors.any { v -> v.id == it.id } } }
                .map {
                    when (it) {
                        true -> 1
                        else -> 0
                    }
                }
        ).toMap().toSortedMap()
    }

    private fun <T> toList(l: T): List<Queue> = when (l) {
        is List<*> -> l as List<Queue>
        else -> listOf<Queue>()
    }
}
