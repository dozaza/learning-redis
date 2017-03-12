package com.github.dozaza.lock

import java.util.UUID
import com.github.dozaza.redis.dsl._

object Lock {

  def acquireLock(name: String, lockTimeout: Int = 10 * 1000, acquireTimeout: Int = 10 * 1000): Option[String] = {
    val uuid = UUID.randomUUID().toString
    val end = System.currentTimeMillis() + acquireTimeout
    val lockName = "lock:" + name
    while(System.currentTimeMillis() < end) {
      val flag = connect { client => client.set(lockName, uuid, NX = true)}
      if (flag) {
        // Update expire time
        connect{ client => client.expire(lockName, lockTimeout) }
        return Some(uuid)
      } else {
        val ttl = connect { client => client.ttl(lockName) }
        // Means lock is gotten by others, but timeout is not set yet
        if (-1 == ttl) {
          // Update expire time
          client.expire(lockName, lockTimeout)
        }
      }
      Thread.sleep(1)
    }

    None
  }

}
