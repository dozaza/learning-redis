
package com.github.dozaza.pubsub

import akka.actor.Actor

import com.github.dozaza.redis.dsl._
import com.github.dozaza.logging.Logging

class PublisherActor extends Actor with Logging {

  override def receive: Receive = {
    case msg: String if msg == "start" =>
      client.publish("test-channel", "hello")
    case msg: String if msg == "pattern" =>
      client.publish("pattern-channel1", "pattern hello")
      client.publish("pattern-channel2", "pattern hello")
    case _ @ x =>
      logger.error("Unknow message: " + x)

  }
}
