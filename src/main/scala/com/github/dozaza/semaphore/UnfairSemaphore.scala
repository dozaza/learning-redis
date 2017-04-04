package com.github.dozaza.semaphore

import java.util.UUID

import com.github.dozaza.redis.dsl._
import redis.api.Limit

import scala.concurrent.ExecutionContext.Implicits.global

object UnfairSemaphore {

  /**
    * This implementation is unfair, cause it use timestamp as identifier and in multi-node system,
    * time system in each node is not same, ex: Node B is 1 second later than Node A, then an acquirement failed in Node A will success for Node B
    * @param name
    * @param limit
    * @param timeout
    * @return
    */
  def acquireSemaphore(name: String, limit: Int, timeout: Int = 10 * 1000): Option[String] = {
    val semaName = "unfair-semaphore:" + name
    val uuid = UUID.randomUUID().toString
    val now = System.currentTimeMillis()

    val result = connect[String] { client =>
      client.zremrangebyscore(semaName, Limit(-1), Limit(now - timeout))
      client.zadd(semaName, (now.toDouble, uuid))
      for {
        rank <- client.zrank(semaName, uuid)
      } yield {
        if (rank.isDefined && rank.get < limit) {
          uuid
        } else {
          null
        }
      }
    }

    Option(result)
  }

}
