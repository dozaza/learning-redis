
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

  def sismember(): Unit = {
    val key = "set-key"
    val (flag, flag2) = connect{ client =>
      client.del(key)
      client.sadd(key, "element1", "element2", "element1")
      for {
        f1 <- client.sismember(key, "element1")
        f2 <- client.sismember(key, "not_exist")
      } yield {
        (f1, f2)
      }
    }
    assertEqual(flag, true)
    assertEqual(flag2, false)
  }

  def scard(): Unit = {
    val key = "set-key"
    val number = connect { client =>
      client.del(key)
      client.sadd(key, "element1", "element2", "element1")
      client.scard(key)
    }
    assertEqual(number, 2)
  }

  def smembers(): Unit = {
    val key = "set-key"
    val members = connect { client =>
      client.del(key)
      client.sadd(key, "element1", "element2", "element1")
      client.smembers[String](key)
    }

    assertEqual(members.size, 2)
    assertEqual(members.contains("element1"), true)
    assertEqual(members.contains("element2"), true)
  }

  def spop(): Unit = {
    val key = "set-key"
    val (p1, p2) = connect { client =>
      client.del(key)
      client.sadd(key, "element1", "element2", "element1")
      client.spop[String](key)

      for {
        p1 <- client.spop[String](key)
        p2 <- client.spop[String](key)
      } yield {
        (p1, p2)
      }
    }

    assertEqual(p1.isDefined, true)
    assertEqual(p2.isEmpty, true)
  }

  def smove(): Unit = {
    val key = "set-key"
    val key2 = "set-key2"
    val (flag, flag2) = connect { client =>
      client.del(key)
      client.sadd(key, "element1", "element2", "element1")
      for {
        f1 <- client.smove(key, key2, "element1")
        f2 <- client.smove(key, key2, "not-exist")
      } yield {
        (f1, f2)
      }
    }

    assertEqual(flag, true)
    assertEqual(flag2, false)
  }
}
