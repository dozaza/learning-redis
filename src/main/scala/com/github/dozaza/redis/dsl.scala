package com.github.dozaza.redis

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import com.github.dozaza.config.TypesafeConfig
import redis.commands.TransactionBuilder
import redis.{RedisBlockingClient, RedisClient}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

package object dsl {

  implicit val akkaSystem = ActorSystem("redis-actor-system", TypesafeConfig.config)

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

  def client = RedisClient()

  /**
    * Send all operations to server only when exec is called, transaction discard will be done automatically when failure
    * @param op
    * @tparam T
    * @return
    */
  def transaction[T](op: TransactionBuilder => Future[T], throwFailure: Boolean = false): Option[Exception] = {
    val redis = RedisClient()
    val tx = redis.transaction()
    try {
      val future = op(tx)
      tx.exec()
      Await.result(future, 1 hours)
      None
    } catch {
      case e: Exception => Some(e)
    }
  }

  /**
    * Send all operations to server only when exec is called, transaction discard will be done automatically when failure
    * @param watchedKeys
    * @param op
    * @tparam T
    * @return
    */
  def transactionWithWatch[T](watchedKeys: String*)(op: TransactionBuilder => Future[T], throwFail: Boolean = false): Option[Exception] = {
    try {
      val redis = RedisClient()
      val tx = redis.transaction()
      tx.watch(watchedKeys:_*)
      val future = op(tx)
      tx.exec()
      Await.result(future, 1 hours)
      None
    } catch {
      case e: Exception => Some(e)
    }
  }

}
