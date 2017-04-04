package com.github.dozaza.semaphore

import java.util.UUID

import com.github.dozaza.redis.dsl._
import redis.api.Limit

object FairSemaphore extends SemaphoreBase {

  /**
    * Try to acquire semphore is "fair" way by adding an auto-increment counter.
    * Attention: This will resolve time mismatching problem in distributed system, but it allow only small error which should be smaller than limit.
    * There is also a concurrency problem need to be considered, cause we use 3 connections(pipeline) to do this.
    * @param name
    * @param limit
    * @param timeout
    * @return
    */
  def acquireSemaphore(name: String, limit: Int, timeout: Int = 10 * 1000): Option[String] = {
    val semaName = "semaphore:" + name
    val uuid = UUID.randomUUID().toString
    val now = System.currentTimeMillis()

    // A zset who contains uuid and its counter value
    val czset = semaName + ":owner"
    // A counter
    val ctr = semaName + ":counter"

    val counter = connect[Long] { client =>
      // remove expired semaphores
      client.zremrangebyscore(semaName, Limit(-1), Limit(now - limit))
      // remove expired semaphores' counter value by doing zinterstore with weight
      client.zinterstoreWeighted(czset, Map(czset -> 1d, semaName -> 0d))
      client.incr(ctr)
    }

    val rankOpt = connect[Option[Long]] { client =>
      client.zadd(semaName, (now, uuid))
      client.zadd(czset, (counter, uuid))

      client.zrank(czset, uuid)
    }

    rankOpt match {
      case Some(_) =>
        // Means semaphore is acquired correctly
        Some(uuid)
      case _ =>
        connect { client =>
          client.zrem(semaName, uuid)
          client.zrem(czset, uuid)
        }
        None
    }
  }

  /**
    * Release fair semaphore.
    * Although there is a "zinterstoreWeighted" before we add a new semaphore, it's better to release it in this method,
    * cause in concurrency, there will be some failures when acquiring semaphore.
    * @param name
    * @param uuid
    */
  def releaseFairSemaphore(name: String, uuid: String): Unit = {
    val semaName = "semaphore:" + name
    val czset = semaName + ":owner"
    connect { client =>
      client.zrem(semaName, uuid)
      client.zrem(czset, uuid)
    }
  }

}
