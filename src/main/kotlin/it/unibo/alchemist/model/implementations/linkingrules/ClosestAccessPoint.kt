package it.unibo.alchemist.model.implementations.linkingrules

import it.unibo.alchemist.model.implementations.neighborhoods.Neighborhoods
import it.unibo.alchemist.model.interfaces.Environment
import it.unibo.alchemist.model.interfaces.Molecule
import it.unibo.alchemist.model.interfaces.Neighborhood
import it.unibo.alchemist.model.interfaces.Node
import it.unibo.alchemist.model.interfaces.Position

/**
 *  Connects to the closest access point.
 *  Access points are connected inside the specified radius.
 *
 *  @param molecule, the molecule that defines whether a node is an access point or not.
 *  @param radius, the radius access points use to connect themselves.
 */
class ClosestAccessPoint<T, P : Position<P>>(private val molecule: Molecule, private val radius: Double) :
    AbstractLocallyConsistentLinkingRule<T, P>() {

    private val Node<T>.isAccessPoint
        get() = contains(molecule)

    override fun computeNeighborhood(center: Node<T>, environment: Environment<T, P>): Neighborhood<T> {
        val (accessPoints, leaves) = environment.nodes.partition { it.isAccessPoint }
        val neighbors: Iterable<Node<T>> = when {
            center.isAccessPoint -> {
                leaves.filter { leaf ->
                    val distance = environment.getDistanceBetweenNodes(center, leaf)
                    accessPoints.none { environment.getDistanceBetweenNodes(center, leaf) < distance }
                } + accessPoints.filter { accessPoint ->
                    environment.getDistanceBetweenNodes(center, accessPoint) < radius && accessPoint.id != center.id
                }
            }
            else -> accessPoints.sortedBy { environment.getDistanceBetweenNodes(center, it) }.take(1)
        }
        return Neighborhoods.make(environment, center, neighbors)
    }
}
