package org.scala.microcity

import it.unibo.alchemist.model.scafi.ScafiIncarnationForAlchemist.StandardSensors
import it.unibo.alchemist.model.implementations.nodes.NodeManager
import org.scala.microcity.Positions.{Coordinates, Position}
import org.scala.microcity.Utils.Molecules.{CAPACITY, QUEUE, SATISFACTION, SATISFIED}

object Utils {
  case class NodeInformation(id: Int, node: NodeManager, sensors: StandardSensors)

  object Molecules {
    val ATTRACTION: String = "attraction"
    val VISITOR: String = "visitor"
    val SATISFIED: String = "satisfied"
    val POSITIONS: String = "positions"
    val SATISFACTION: String = "satisfaction"
    val QUEUE: String = "queue"
    val QUEUES: String = "queues"
    val CAPACITY: String = "capacity"
    val SATISFACTIONS: String = "satisfactions"
    val DESTINATION: String = "destination"
  }

  def role(role: String)(implicit node: NodeManager): Boolean = node.has(role) && node.get[Boolean](role)

  def coordinates(implicit node: StandardSensors): Coordinates = node.currentPosition()

  def satisfied(implicit node: NodeManager): Boolean = node.has(SATISFIED) && node.get[Boolean](SATISFIED)

  def dissatisfy(currentDestination: Coordinates, currentPosition: Coordinates)(implicit node: NodeManager): Unit =
    satisfy(currentDestination == currentPosition)

  def satisfy(satisfied: Boolean)(implicit node: NodeManager): Unit = node.put(SATISFIED, satisfied)

  def getSatisfied(implicit node: NodeManager): List[Position] =
    if (node.has(SATISFACTION)) node.get[List[Position]](SATISFACTION) else List()

  def getQueue(implicit node: NodeManager): List[Position] =
    if (node.has(QUEUE)) node.get[List[Position]](QUEUE) else List()

  def getVisitorsPerRound(implicit node: NodeManager): Int =
    node.get[Int](CAPACITY)

}
