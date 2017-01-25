
package com.github.dozaza.base

import com.github.dozaza.connect.dsl._
import com.github.dozaza.test.TestModule
import redis.api.Limit

object ZSetOperation extends TestModule {

  def zadd(): Unit = {
    val key = "test-zset"
    val flag = connect[Long]{ client =>
      client.del(key)
      client.zadd(key, (2.22, "element2"), (1.11, "element1"), (2.22, "element2"), (10.10, "element10"))
    }
    assertEqual(flag, 3)
  }

  def zrange(): Unit = {
    val key = "test-zset"
    val seq = connect[Seq[String]]{ client =>
      client.del(key)
      client.zadd(key, (2.22, "element2"), (1.11, "element1"), (10.10, "element10"))
      client.zrange[String](key, 0, 1)
    }
    assertEqual(seq.size, 2)
    assertEqual(seq.head, "element1")
  }

  def zrangescore(): Unit = {
    val key = "test-zset"
    val seq = connect[Seq[String]]{ client =>
      client.del(key)
      client.zadd(key, (2.22, "element2"), (1.11, "element1"), (10.10, "element10"))
      client.zrangebyscore[String](key, Limit(2.22, inclusive = true), Limit(10.10, inclusive = false))
    }
    assertEqual(seq.size, 1)
    assertEqual(seq.head, "element2")
  }

  def zrem(): Unit = {
    val key = "test-zset"
    val flag = connect[Long] { client =>
      client.del(key)
      client.zadd(key, (2.22, "element2"), (1.11, "element1"), (10.10, "element10"))
      client.zrem(key, "element1")
    }
    assertEqual(flag, 1)
  }
}
