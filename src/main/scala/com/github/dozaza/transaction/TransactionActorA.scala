
package com.github.dozaza.transaction

import akka.actor.Actor
import com.github.dozaza.redis.dsl._
import com.github.dozaza.logging.Logging
import com.github.dozaza.test.TestModule


class TransactionActorA  extends Actor with Logging with TestModule {

  override def receive: Receive = {
    case msg: String if msg == "start" =>
      logger.info("TransactionActorA is started")
      val exceptionOpt = transactionWithWatch("test-tx") { tx =>
        // May be a better way to let redis server "wait" for a while
        (0 until 10000).foreach { _ => tx.get("test-tx")}
        tx.set("test-tx", "hello tx")
      }
      assertEqual(exceptionOpt.isEmpty, true)
      val value = connect { client => client.get[String]("test-tx") }
      assertEqual(value, Some("hello tx2"))
    case _@x => logger.error("Unknown message: " + x)
  }

}
