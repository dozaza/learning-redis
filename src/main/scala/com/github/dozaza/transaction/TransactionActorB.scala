
package com.github.dozaza.transaction

import akka.actor.Actor
import com.github.dozaza.logging.Logging
import com.github.dozaza.redis.dsl._
import com.github.dozaza.test.TestModule


class TransactionActorB  extends Actor with Logging with TestModule {

  override def receive: Receive = {
    case msg: String if msg == "start" =>
      val value = connect { client =>
        client.del("test-tx")
        client.set("test-tx", "hello tx2")
        client.get[String]("test-tx")
      }
      assertEqual(value, Some("hello tx2"))
    case _@x => logger.error("Unknown message: " + x)
  }

}
