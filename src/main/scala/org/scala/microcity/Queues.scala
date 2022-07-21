package org.scala.microcity

import it.unibo.alchemist.model.implementations.nodes.NodeManager
import org.scala.microcity.Positions.{Position, position, toCoordinates}
import org.scala.microcity.Utils.Molecules.{ATTRACTION, SATISFACTIONS, VISITOR}
import org.scala.microcity.Utils.{NodeInformation, getQueue, getSatisfied, getVisitorsPerRound, role}

object Queues {
  def queueUnion(l1: List[Position], l2: List[Position])(implicit info: NodeInformation): List[Position] =
    if (role(ATTRACTION)(info.node))
      (l1 ++ l2).distinct
        .filter(_.coordinates == toCoordinates(info.sensors.currentPosition()))
        .filter(_.id != info.id)
        .filterNot( p => getSatisfied(info.node).map(_.id).contains(p.id))
    else
      position(info.id, info.sensors.currentPosition())(info.node)

  def dequeue(implicit node: NodeManager): List[Position] =
    if (role(ATTRACTION)) getQueue.take(getVisitorsPerRound.min(getQueue.size))
    else List()

  def satisfactionUnion(l1: List[Position], l2: List[Position])(implicit info: NodeInformation): List[Position] =
    if (role(VISITOR)(info.node)) (l1 ++ l2).distinct.filter(_.id == info.id)
    else (l1 ++ l2).distinct.filter(_.coordinates == toCoordinates(info.sensors.currentPosition()))

  def addSatisfaction(value: Boolean)(implicit node: NodeManager): Unit =
    if (value) node.put(SATISFACTIONS, node.get[Int](SATISFACTIONS) + 1)
}
