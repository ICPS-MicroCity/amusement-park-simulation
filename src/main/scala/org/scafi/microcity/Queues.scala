package org.scafi.microcity

import it.unibo.alchemist.model.scafi.ScafiIncarnationForAlchemist.{AggregateProgram, ScafiAlchemistSupport, StandardSensors}
import org.scala.microcity.Queues.{createQueue, queuesUnion}
import org.scala.microcity.Utils.NodeInformation

class Queues extends AggregateProgram with StandardSensors with ScafiAlchemistSupport {
  override def main(): Any = {
    implicit val i: NodeInformation = NodeInformation(mid(), this.node, this)

    rep(createQueue) { queue =>
      foldhood(createQueue)(queuesUnion) {
        nbr { queue }
      }
    }
  }
}
