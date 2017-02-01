package com.github.dozaza.redis

import com.typesafe.config.ConfigFactory

import akka.actor.ActorSystem
import redis.{RedisBlockingClient, RedisClient}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

package object dsl {

  implicit val actorSystem = ActorSystem("redis-actor-system", ConfigFactory.load())

  def connect[T](op: RedisClient => Future[T]): T = {
    val redis = RedisClient()
    val future = op(redis)
    val result = Await.result(future, 1 hours)
    result
  }

  def connectWithBlock[T](op: RedisBlockingClient => Future[T]): T = {
    val blockClient = RedisBlockingClient()
    val future = op(blockClient)
    val result = Await.result(future, 1 hours)
    result
  }

}
