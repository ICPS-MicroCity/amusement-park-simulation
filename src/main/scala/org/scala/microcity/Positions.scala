package org.scala.microcity

import it.unibo.alchemist.model.implementations.nodes.NodeManager
import it.unibo.alchemist.model.scafi.ScafiIncarnationForAlchemist.P
import org.scala.microcity.Utils.Molecules.{ATTRACTION, POSITIONS}
import org.scala.microcity.Utils.role

import scala.language.implicitConversions

object Positions {
  case class Coordinates(latitude: Double, longitude: Double)
  case class Position(id: Int, coordinates: Coordinates)

  def attractionPosition(id: => Int, position: => P)(implicit node: NodeManager): List[Position] =
    if (role(ATTRACTION)) List(Position(id, position)) else List()

  def position(id: Int, position: P)(implicit node: NodeManager): List[Position] =
    List(Position(id, position))

  def attractionUnion(l1: List[Position], l2: List[Position]): List[Position] =
    (l1 ++ l2).distinct.sortBy(_.id)

  def attractionPositions(implicit node: NodeManager): List[Position] =
    if (node.has(POSITIONS)) node.get[List[Position]](POSITIONS) else List()

  implicit def toCoordinates(p: P): Coordinates = Coordinates(round(p._1), round(p._2))

  def round(d: Double)(implicit r: Int = 100000): Double = (d * r).round.toDouble / r.toDouble
}
