package org.scafi.microcity

import it.unibo.alchemist.model.implementations.nodes.NodeManager
import it.unibo.alchemist.model.scafi.ScafiIncarnationForAlchemist.{AggregateProgram, ScafiAlchemistSupport, StandardSensors}
import org.scala.microcity.Destination.{NextPolicy, RandomPolicy}
import org.scala.microcity.Utils.{coordinates, dissatisfy, satisfied}

class Destination extends AggregateProgram with StandardSensors with ScafiAlchemistSupport {
  override def main(): Any = {
    implicit val n: NodeManager = this.node
    implicit val s: StandardSensors = this
    val nextPolicy: NextPolicy = RandomPolicy

    rep(nextPolicy.next(coordinates)) { dest =>
      if (satisfied) {
        dissatisfy(dest, coordinates)
      }
      nextPolicy.next(dest)
    }
  }
}
