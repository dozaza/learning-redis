
package com.github.dozaza.pubsub

import java.net.InetSocketAddress

import com.github.dozaza.logging.Logging
import redis.actors.RedisSubscriberActor
import redis.api.pubsub.{Message, PMessage}


class SubscriberActor(channels: Seq[String] = Nil, patterns: Seq[String] = Nil)
  extends RedisSubscriberActor(new InetSocketAddress("localhost", 6379), channels, patterns, onConnectStatus = connected => { println(s"connected: $connected")}) with Logging {

  override def onMessage(m: Message): Unit = {
    logger.info("message received: " + m.data.decodeString("utf-8"))
  }

  override def onPMessage(pm: PMessage): Unit = {
    logger.info("Pattern message received: " + pm.data.decodeString("utf-8"))
  }

}
