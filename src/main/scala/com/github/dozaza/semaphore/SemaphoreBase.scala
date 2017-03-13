package com.github.dozaza.semaphore

import com.github.dozaza.redis.dsl._

trait SemaphoreBase {

  def releaseSemaphore(name: String, uuid: String): Unit = {
    val semaName = "semaphore:" + name
    connect{ client => client.zrem(semaName, uuid) }
  }

}
