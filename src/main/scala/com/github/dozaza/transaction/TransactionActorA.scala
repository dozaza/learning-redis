
package com.github.dozaza.transaction

import akka.actor.Actor
import com.github.dozaza.redis.dsl._
import com.github.dozaza.logging.Logging
import com.github.dozaza.test.TestModule


class TransactionActorA  extends Actor with Logging with TestModule {

  override def receive: Receive = {
    case msg: String if msg == "start" =>
      val exceptionOpt = transactionWithWatch("test-tx") { tx =>
        tx.del("test-tx")
        // May be a better way to let redis server "wait" for a while
        (0 until 100000).foreach { _ =>
          tx.set("test-tx", "hello tx")
        }
        tx.get[String]("test-tx")
      }
      assertEqual(exceptionOpt.isDefined, true)
    case _@x => logger.error("Unknown message: " + x)
  }

}
