package org.scala.microcity

import it.unibo.alchemist.model.implementations.nodes.NodeManager
import org.scala.microcity.Positions.{Coordinates, attractionPositions}
import org.scala.microcity.Utils.satisfied

import scala.util.Random

object Destination {
  trait NextPolicy {
    def next(currentDestination: Coordinates)(implicit node: NodeManager): Coordinates
  }

  case object RandomPolicy extends NextPolicy {
    override def next(currentDestination: Coordinates)(implicit node: NodeManager): Coordinates =
      if (satisfied && attractionPositions.nonEmpty)
        attractionPositions.apply(Random.nextInt(attractionPositions.size)).coordinates
      else currentDestination
  }
}
