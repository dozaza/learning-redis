
package com.github.dozaza.transaction

import akka.actor.Actor
import com.github.dozaza.redis.dsl._
import com.github.dozaza.logging.Logging
import com.github.dozaza.test.TestModule


/**
  * Maybe try to make some tests for transaction execution failure
  */
class TransactionActorA  extends Actor with Logging with TestModule {

  override def receive: Receive = {
    case msg: String if msg == "start" =>
      transactionWithWatch("test-tx") { tx =>
        tx.del("test-tx")
        tx.set("test-tx", "hello tx")
        tx.get[String]("test-tx")
      } match {
        case Some(value) => assertEqual(value, Some("hello tx"))
        case _ => logger.error("Unable to execute transaction")
      }

    case _@x => logger.error("Unknown message: " + x)
  }

}
