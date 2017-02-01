
package com.github.dozaza.pubsub

import akka.actor.Props
import com.github.dozaza.redis.dsl._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Pubsub {

  private val publisher = akkaSystem.actorOf(Props[PublisherActor], name = "publisher-actor")

  def start(): Unit = {
    val channels = "test-channel" :: Nil
    val patterns = """pattern-channel*""" :: Nil
    akkaSystem.actorOf(Props(classOf[SubscriberActor], channels, patterns).withDispatcher("rediscala.rediscala-client-worker-dispatcher"))

    akkaSystem.scheduler.scheduleOnce(1 seconds, publisher, "start")
    akkaSystem.scheduler.scheduleOnce(1 seconds, publisher, "pattern")


  }
}
