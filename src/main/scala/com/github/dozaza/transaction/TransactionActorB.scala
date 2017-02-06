
package com.github.dozaza.transaction

import akka.actor.Actor
import com.github.dozaza.logging.Logging
import com.github.dozaza.redis.dsl._
import com.github.dozaza.test.TestModule

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class TransactionActorB  extends Actor with Logging with TestModule {

  override def receive: Receive = {
    case msg: String if msg == "start" =>
      val value = transaction { tx =>
        akkaSystem.scheduler.scheduleOnce(1 seconds, self, "wait")
        tx.get("test-tx")
      }
      assertEqual(value, "hello tx")
    case msg: String if msg == "wait" => // Do nothing
    case _@x => logger.error("Unknow message: " + x)
  }

}
