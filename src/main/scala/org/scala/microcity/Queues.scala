package org.scala.microcity

import it.unibo.alchemist.model.implementations.nodes.NodeManager
import it.unibo.alchemist.model.scafi.ScafiIncarnationForAlchemist.AggregateProgram
import org.scala.microcity.Positions.{Position, position, toCoordinates}
import org.scala.microcity.Utils.Molecules.{ATTRACTION, SATISFACTIONS, VISITOR}
import org.scala.microcity.Utils._

object Queues extends AggregateProgram {
  case class Queue(attraction: Position, visitors: Int)

  def queueUnion(l1: List[Position], l2: List[Position])(implicit info: NodeInformation): List[Position] =
    mux(role(ATTRACTION)(info.node)) {
      (l1 ++ l2).distinct
        .filter(_.coordinates == toCoordinates(info.sensors.currentPosition()))
        .filter(_.id != info.id)
        .filterNot(p => getSatisfied(info.node).map(_.id).contains(p.id))
    } {
      position(info.id, info.sensors.currentPosition())(info.node)
    }

  def queuesUnion(l1: List[Queue], l2: List[Queue])(implicit info: NodeInformation): List[Queue] =
    (l1 ++ l2).distinct.sortBy(_.attraction.id)

  def dequeue(implicit node: NodeManager): List[Position] =
    mux(role(ATTRACTION)) {
      getQueue.take(getVisitorsPerRound.min(getQueue.size))
    } {
      List()
    }

  def satisfactionUnion(l1: List[Position], l2: List[Position])(implicit info: NodeInformation): List[Position] =
    mux(role(VISITOR)(info.node)) {
      (l1 ++ l2).distinct.filter(_.id == info.id)
    } {
      (l1 ++ l2).distinct.filter(_.coordinates == toCoordinates(info.sensors.currentPosition()))
    }

  def addSatisfaction(value: Boolean)(implicit node: NodeManager): Unit =
    mux(value) { node.put(SATISFACTIONS, node.get[Int](SATISFACTIONS) + 1) } {}

  def createQueue(implicit info: NodeInformation): List[Queue] =
    mux(role(ATTRACTION)(info.node)) {
      List(Queue(Position(info.id, Utils.coordinates(info.sensors)), getQueue(info.node).size))
    } {
      List()
    }

  override def main(): Any = ???
}
