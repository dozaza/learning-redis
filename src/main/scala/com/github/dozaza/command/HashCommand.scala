
package com.github.dozaza.command

import com.github.dozaza.redis.dsl._
import com.github.dozaza.test.TestModule

import scala.concurrent.ExecutionContext.Implicits.global


object HashCommand extends TestModule {

  def hmgetAndHmset(): Unit = {
    val key = "hash-key"
    val results = connect { client =>
      client.del(key)
      client.hmset(key, Map("name" -> "dozaza", "sex" -> "male"))
      client.hmget[String](key, "name", "not-exist")
    }
    assertEqual(results.head, Some("dozaza"))
    assertEqual(results.last, None)
  }

  def hdel(): Unit = {
    val key = "hash-key"
    val results = connect { client =>
      client.del(key)
      client.hmset(key, Map("name" -> "dozaza", "sex" -> "male"))
      client.hdel(key, "sex")
      client.hmget(key, "sex")
    }
    assertEqual(results.head, None)
  }

  def hexists(): Unit = {
    val key = "hash-key"
    val (flag, flag2) = connect { client =>
      client.del(key)
      client.hmset(key, Map("name" -> "dozaza", "sex" -> "male"))
      for {
        f1 <- client.hexists(key, "name")
        f2 <- client.hexists(key, "not-exist")
      } yield {
        (f1, f2)
      }
    }

    assertEqual(flag, true)
    assertEqual(flag2, false)
  }

  def hkeys(): Unit = {
    val key = "hash-key"
    val keys = connect { client =>
      client.del(key)
      client.hmset(key, Map("name" -> "dozaza", "sex" -> "male"))
      client.hkeys(key)
    }

    assertEqual(keys.size, 2)
  }

  def hvals(): Unit = {
    val key = "hash-key"
    val vals = connect { client =>
      client.del(key)
      client.hmset(key, Map("name" -> "dozaza", "sex" -> "male"))
      client.hvals[String](key)
    }

    assertEqual(vals.size, 2)
  }

  def hgetall(): Unit = {
    val key = "hash-key"
    val map = connect { client =>
      client.del(key)
      client.hmset(key, Map("name" -> "dozaza", "sex" -> "male"))
      client.hgetall[String](key)
    }

    assertEqual(map.size, 2)
  }

  def hincrby(): Unit = {
    val key = "hash-key"
    val result = connect { client =>
      client.del(key)
      client.hmset(key, Map("k1" -> 1, "k2" -> 2))
      client.hincrby(key, "k1", 10)
      client.hmget[Double](key, "k1")
    }

    assertEqual(result.head, Some(11))
  }

  def hincrbyfloat(): Unit = {
    val key = "hash-key"
    val result = connect { client =>
      client.del(key)
      client.hmset(key, Map("k1" -> 1.0, "k2" -> 2.0))
      client.hincrbyfloat(key, "k1", 10.5)
      client.hmget[Double](key, "k1")
    }

    assertEqual(result.head, Some(11.5))
  }
}
