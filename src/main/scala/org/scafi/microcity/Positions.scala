package org.scafi.microcity

import it.unibo.alchemist.model.implementations.nodes.NodeManager
import it.unibo.alchemist.model.scafi.ScafiIncarnationForAlchemist.{AggregateProgram, CustomSpawn, ScafiAlchemistSupport, StandardSensors}
import org.scala.microcity.Destination.{NextPolicy, RandomPolicy}
import org.scala.microcity.Positions.{Position, attractionUnion, position}
import org.scala.microcity.Queues._
import org.scala.microcity.Utils.Molecules._
import org.scala.microcity.Utils._

class Positions extends AggregateProgram with StandardSensors with ScafiAlchemistSupport with CustomSpawn {
  override def main(): Any = {
    implicit val n: NodeManager = this.node
    implicit val s: StandardSensors = this
    implicit val i: NodeInformation = NodeInformation(mid(), n, s)
    val nextPolicy: NextPolicy = RandomPolicy

    node.put(QUEUES, share(mux(role(ATTRACTION)) {
      List(Queue(Position(mid(), currentPosition()), getQueue(node).size))
    } {
      List()
    }) { (queue, nbrQueue) =>
      foldhood(queue)(queuesUnion) {
        nbrQueue()
      }
    })

    node.put(POSITIONS, rep(
      mux(role(ATTRACTION)) {
        List(Position(mid(), currentPosition()))
      } {
        List()
      }) { pos =>
        foldhood(List[Position]())(attractionUnion) {
          nbr { pos }
        }
    })

    node.put(DESTINATION, rep(nextPolicy.next(coordinates)) { dest =>
      mux(satisfied) {
        dissatisfy(dest, coordinates)
      } {}
      nextPolicy.next(dest)
    })

    node.put(QUEUE, rep(position(mid(), currentPosition())) { pos =>
      foldhood(pos)(queueUnion) {
        nbr(pos)
      }
    })

    node.put(SATISFACTION, rep(dequeue) { pos =>
      val satisfied = foldhoodPlus(pos)(satisfactionUnion) {
        nbr(pos)
      }
      satisfy(satisfied.nonEmpty)
      addSatisfaction(satisfied.nonEmpty)
      dequeue
    })

  }
}
