package org.scafi.microcity

import it.unibo.alchemist.model.implementations.nodes.NodeManager
import it.unibo.alchemist.model.scafi.ScafiIncarnationForAlchemist.{AggregateProgram, ScafiAlchemistSupport, StandardSensors}
import org.scala.microcity.Destination.{NextPolicy, RandomPolicy}
import org.scala.microcity.Positions.{Position, attractionPosition, attractionUnion, position}
import org.scala.microcity.Queues.{addSatisfaction, dequeue, queueUnion, satisfactionUnion}
import org.scala.microcity.Utils.Molecules.{ATTRACTION, POSITIONS, QUEUE, SATISFACTION}
import org.scala.microcity.Utils.{NodeInformation, coordinates, dissatisfy, role, satisfied, satisfy}

class Positions extends AggregateProgram with StandardSensors with ScafiAlchemistSupport {
  override def main(): Any = {
    implicit val n: NodeManager = this.node
    implicit val s: StandardSensors = this
    implicit val i: NodeInformation = NodeInformation(mid(), n, s)
    val nextPolicy: NextPolicy = RandomPolicy

    rep(branch(role(ATTRACTION)) {
      List(Position(mid(), currentPosition()))
    } {
      List()
    }) { pos =>
      foldhood(List[Position]())(attractionUnion) {
        nbr {
          pos
        }
      }
    }

  }
}
