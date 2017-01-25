package com.github.dozaza.connect

import redis.RedisClient

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

package object dsl {

  def connect[T](op: RedisClient => Future[T]): T = {
    implicit val akkaSystem = akka.actor.ActorSystem()
    val redis = RedisClient()
    val future = op(redis)
    val result = Await.result(future, 1 hours)
    akkaSystem.shutdown()
    result
  }

}
