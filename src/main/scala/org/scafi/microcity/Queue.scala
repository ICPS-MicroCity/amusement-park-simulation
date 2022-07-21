package org.scafi.microcity

import it.unibo.alchemist.model.implementations.nodes.NodeManager
import it.unibo.alchemist.model.scafi.ScafiIncarnationForAlchemist.{AggregateProgram, ScafiAlchemistSupport, StandardSensors}
import org.scala.microcity.Positions.position
import org.scala.microcity.Queues.queueUnion
import org.scala.microcity.Utils.NodeInformation

class Queue extends AggregateProgram with StandardSensors with ScafiAlchemistSupport {
  override def main(): Any = {
    implicit val n: NodeManager = this.node
    implicit val s: StandardSensors = this
    implicit val i: NodeInformation = NodeInformation(mid(), n, s)

    rep(position(mid(), currentPosition())) { pos =>
      foldhood(pos)(queueUnion) {
        nbr(pos)
      }
    }
  }
}
