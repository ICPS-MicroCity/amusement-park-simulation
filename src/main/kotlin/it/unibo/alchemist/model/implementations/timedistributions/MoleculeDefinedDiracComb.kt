package it.unibo.alchemist.model.implementations.timedistributions

import it.unibo.alchemist.model.interfaces.Molecule
import it.unibo.alchemist.model.interfaces.Node

/**
 * A DiracComb time distribution whose time is defined by the value of a molecule.
 */
class MoleculeDefinedDiracComb<T>(node: Node<T>, molecule: Molecule) :
    DiracComb<T>(node.getConcentration(molecule) as Double)
