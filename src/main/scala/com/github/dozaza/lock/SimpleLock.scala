package com.github.dozaza.lock

import java.util.UUID
import com.github.dozaza.redis.dsl._
import scala.concurrent.ExecutionContext.Implicits.global

object SimpleLock {

  /**
    * acquire a lock with a timeout on milliseconds, if succeeded, return Some(UUID), else return None
    * @param name
    * @param timeout
    * @return
    */
  def acquireLock(name: String, timeout: Int = 10 * 1000): Option[String] = {
    val uuid = UUID.randomUUID().toString
    val end = System.currentTimeMillis() + timeout
    val lockName = "lock:" + name
    while (System.currentTimeMillis() < end)  {
      val result = connect { client =>
        for (flag <- client.set(lockName, uuid, NX = true)) yield {
          if (flag) {
            uuid
          } else {
            null
          }
        }
      }
      if (result != null) {
        return Some(result)
      }
      Thread.sleep(1)
    }
    None
  }

}
