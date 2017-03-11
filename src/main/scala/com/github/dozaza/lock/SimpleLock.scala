package com.github.dozaza.lock

import java.util.UUID
import com.github.dozaza.redis.dsl._


object SimpleLock {

  /**
    * try acquire a lock with a timeout on milliseconds, if succeeded, return Some(UUID), else return None
    * @param name
    * @param timeout
    * @return
    */
  def tryAcquireLock(name: String, timeout: Int = 10 * 1000): Option[String] = {
    val uuid = UUID.randomUUID().toString
    val end = System.currentTimeMillis() + timeout
    while (System.currentTimeMillis() < end)  {
      val result = connect { client =>
        for (flag <- client.set(name, uuid, NX = true)) yield {
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
