package it.unibo.alchemist.model.implementations.timedistributions

import it.unibo.alchemist.model.interfaces.Molecule
import it.unibo.alchemist.model.interfaces.Node

class MoleculeDefinedDiracComb<T>(node: Node<T>, molecule: Molecule)
    : DiracComb<T>(node.getConcentration(molecule) as Double)