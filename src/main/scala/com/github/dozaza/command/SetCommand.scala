
package com.github.dozaza.command

import com.github.dozaza.redis.dsl._
import com.github.dozaza.test.TestModule

import scala.concurrent.ExecutionContext.Implicits.global

object SetCommand extends TestModule {

  def sadd(): Unit = {
    val key = "set-key"
    val flag = connect{ client =>
      client.del(key)
      client.sadd(key, "element1", "element2", "element1")
    }
    assertEqual(flag, 2)
  }

  def srem(): Unit = {
    val key = "set-key"
    val (flag, flag2) = connect{ client =>
      client.del(key)
      client.sadd(key, "element1", "element2", "element1")
      for {
        f1 <- client.srem(key, "element1")
        f2 <- client.srem(key, "element1")
      } yield {
        (f1, f2)
      }
    }
    assertEqual(flag, 1)
    assertEqual(flag, 0)
  }

}
