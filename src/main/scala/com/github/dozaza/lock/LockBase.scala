package com.github.dozaza.lock

import com.github.dozaza.redis.dsl._

trait LockBase {

  def release(name: String, uuid: String): Unit = {
    val lockName = "lock:" + name

    (0 until 10).foreach { _ =>
      val lockedUuidOpt = connect { client => client.get[String](lockName) }
      if (lockedUuidOpt.isDefined && lockedUuidOpt.get == uuid) {
        val exceptionOpt = transactionWithWatch(lockName) { client => client.del(lockName) }
        if (exceptionOpt.isEmpty) {
          return
        }
      }

      Thread.sleep(100)
    }
  }

}
