
package com.github.dozaza.test

import akka.actor.Props
import com.github.dozaza.redis.dsl._
import com.github.dozaza.transaction.{TransactionActorA, TransactionActorB}

object TransactionTest {

  def start(): Unit = {
    val actorA = akkaSystem.actorOf(Props(classOf[TransactionActorA]), "ActorA")
    val actorB = akkaSystem.actorOf(Props(classOf[TransactionActorB]), "ActorB")

    actorA ! "start"
    actorB ! "start"
  }

}
