package org.scafi.microcity

import it.unibo.alchemist.model.implementations.nodes.NodeManager
import it.unibo.alchemist.model.scafi.ScafiIncarnationForAlchemist.{AggregateProgram, ScafiAlchemistSupport, StandardSensors}
import org.scala.microcity.Queues.{addSatisfaction, dequeue, satisfactionUnion}
import org.scala.microcity.Utils.{NodeInformation, satisfy}

class Satisfaction extends AggregateProgram with StandardSensors with ScafiAlchemistSupport{
  override def main(): Any = {
    implicit val n: NodeManager = this.node
    implicit val i: NodeInformation = NodeInformation(mid(), n, this)

    rep(dequeue) { pos =>
      val satisfied = foldhoodPlus(pos)(satisfactionUnion) {
        nbr(pos)
      }
      satisfy(satisfied.nonEmpty)
      addSatisfaction(satisfied.nonEmpty)
      dequeue
    }
  }
}
