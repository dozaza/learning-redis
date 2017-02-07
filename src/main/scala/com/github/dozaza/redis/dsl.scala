package com.github.dozaza.redis

import akka.actor.ActorSystem
import com.github.dozaza.config.TypesafeConfig
import redis.commands.TransactionBuilder
import redis.{RedisBlockingClient, RedisClient}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.control.NonFatal

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
    * Send all operations to server only when exec is called
    * @param op
    * @tparam T
    * @return
    */
  def transaction[T](op: TransactionBuilder => Future[T]): Option[Throwable] = {
    val redis = RedisClient()
    val tx = redis.transaction()
    try {
      val future = op(tx)
      tx.exec()
      Await.result(future, 1 hours)
      None
    } catch {
      case NonFatal(e) =>
        tryDiscard(tx)
        Some(e)
    }
  }

  /**
    * Send all operations to server only when exec is called
    * @param watchedKeys
    * @param op
    * @tparam T
    * @return
    */
  def transactionWithWatch[T](watchedKeys: String*)(op: TransactionBuilder => Future[T]): Option[Throwable] = {
    val redis = RedisClient()
    val tx = redis.transaction()
    try {
      tx.watch(watchedKeys:_*)
      val future = op(tx)
      tx.exec()
      Await.result(future, 1 hours)
      None
    } catch {
      case NonFatal(e) =>
        tryDiscard(tx)
        Some(e)
    }
  }

  private def tryDiscard(transactionBuilder: TransactionBuilder): Unit = {
    try {
      transactionBuilder.discard()
    } catch {
      case _: IllegalStateException => // Do nothing cause already closed
      case x: Throwable => throw x
    }
  }

}
