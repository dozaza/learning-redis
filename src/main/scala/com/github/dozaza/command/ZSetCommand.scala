
package com.github.dozaza.command

import com.github.dozaza.redis.dsl._
import com.github.dozaza.test.TestModule
import redis.api.{Limit, MAX, SUM}

import scala.concurrent.ExecutionContext.Implicits.global

object ZSetCommand extends TestModule {

  def zadd(): Unit = {
    val key = "zset-key"
    val result = connect { client =>
      client.del(key)
      client.zadd(key, (1, "k1"), (2, "k2"))
    }

    assertEqual(result, 2)
  }

  def zrem(): Unit = {
    val key = "zset-key"
    val (result1, result2) = connect { client =>
      client.del(key)
      client.zadd(key, (1, "k1"), (2, "k2"))
      for {
        r1 <- client.zrem(key, "k1", "k2")
        r2 <- client.zrem(key, "k1")
      } yield {
        (r1, r2)
      }
    }

    assertEqual(result1, 2)
    assertEqual(result2, 0)
  }

  def zcard(): Unit = {
    val key = "zset-key"
    val result = connect { client =>
      client.del(key)
      client.zadd(key, (1, "k1"), (2, "k2"))
      client.zcard(key)
    }

    assertEqual(result, 2)
  }

  /**
    * Add score to an element
    */
  def zincrby(): Unit = {
    val key = "zset-key"
    val range = connect { client =>
      client.del(key)
      client.zadd(key, (1, "k1"), (2, "k2"))
      client.zincrby(key, 10, "k1")
      client.zrangeWithscores[String](key, 0, 0)
    }

    assertEqual(range.head._1, "k2")
    assertEqual(range.head._2, 2)
  }

  /**
    * Return number of elements whose score is in the range(including min & max)
    */
  def zcount(): Unit = {
    val key = "zset-key"
    val count = connect { client =>
      client.del(key)
      client.zadd(key, (1, "k1"), (2, "k2"), (10, "k10"))
      client.zcount(key, Limit(1), Limit(2))
    }

    assertEqual(count, 2)
  }

  /**
    * Return the rank of element by score, 0-based
    */
  def zrank(): Unit = {
    val key = "zset-key"
    val rank = connect { client =>
      client.del(key)
      client.zadd(key, (1, "k1"), (2, "k2"), (10, "k10"))
      client.zrank(key, "k10")
    }

    assertEqual(rank, Some(2))
  }

  def zscore(): Unit = {
    val key = "zset-key"
    val score = connect { client =>
      client.del(key)
      client.zadd(key, (1, "k1"), (2, "k2"), (10, "k10"))
      client.zscore(key, "k10")
    }

    assertEqual(score, Some(10d))
  }

  /**
    * Return a range of elements by index. If want to return with score, use zrangeWithscores
    */
  def zrange(): Unit = {
    val key = "zset-key"
    val range = connect { client =>
      client.del(key)
      client.zadd(key, (1, "k1"), (2, "k2"), (10, "k10"))
      client.zrange[String](key, 0, 1)
    }

    assertEqual(range.size, 2)
    assertEqual(range.head, "k1")
    assertEqual(range.last, "k2")
  }

  /**
    * Return the reverse rank of element
    */
  def zrevrank(): Unit = {
    val key = "zset-key"
    val rank = connect { client =>
      client.del(key)
      client.zadd(key, (1, "k1"), (2, "k2"), (10, "k10"))
      client.zrevrank(key, "k10")
    }

    assertEqual(rank, Some(0))
  }

  /**
    * Return a range of elements by index in reverse order by score. If want to return with score, use zrevrangeWithscores
    */
  def zrevrange(): Unit = {
    val key = "zset-key"
    val range = connect { client =>
      client.del(key)
      client.zadd(key, (1, "k1"), (2, "k2"), (10, "k10"))
      client.zrevrange[String](key, 0, 1)
    }

    assertEqual(range.size, 2)
    assertEqual(range.head, "k10")
    assertEqual(range.last, "k2")
  }

  def zrangebyscore(): Unit = {
    val key = "zset-key"
    val range = connect { client =>
      client.del(key)
      client.zadd(key, (1, "k1"), (2, "k2"), (10, "k10"))
      client.zrangebyscore[String](key, Limit(5), Limit(10))
    }

    assertEqual(range.size, 1)
    assertEqual(range.head, "k10")
  }

  def zrevrangebyscore(): Unit = {
    val key = "zset-key"
    val range = connect { client =>
      client.del(key)
      client.zadd(key, (1, "k1"), (2, "k2"), (10, "k10"))
      client.zrevrangebyscore[String](key, Limit(2), Limit(10))
    }

    assertEqual(range.size, 2)
    assertEqual(range.head, "k10")
    assertEqual(range.last, "k2")
  }

  def zremrangebyrank(): Unit = {
    val key = "zset-key"
    val range = connect { client =>
      client.del(key)
      client.zadd(key, (1, "k1"), (2, "k2"), (10, "k10"))
      client.zremrangebyrank(key, 0, 1)
      client.zrange[String](key, 0, -1)
    }

    assertEqual(range.size, 1)
    assertEqual(range.head, "k10")
  }

  def zremrangebyscore(): Unit = {
    val key = "zset-key"
    val range = connect { client =>
      client.del(key)
      client.zadd(key, (1, "k1"), (2, "k2"), (10, "k10"))
      client.zremrangebyscore(key, Limit(0), Limit(2))
      client.zrange[String](key, 0, -1)
    }

    assertEqual(range.size, 1)
    assertEqual(range.head, "k10")
  }

  def zinterstore(): Unit = {
    val key = "zset-key"
    val key2 = "zset-key2"
    val dest = "dest-zset-key"
    val result = connect { client =>
      client.del(key)
      client.del(key2)
      client.del(dest)
      client.zadd(key, (1, "k1"), (2, "k2"), (10, "k10"))
      client.zadd(key2, (5, "k1"), (6, "k2"), (8, "k8"))

      client.zinterstore(dest, key, Seq(key2), MAX)
      client.zrangeWithscores[String](dest, 0, -1)
    }

    assertEqual(result.size, 2)
    assertEqual(result.head._1, "k1")
    assertEqual(result.head._2, 5)
    assertEqual(result.last._1, "k2")
    assertEqual(result.last._2, 6)
  }

  def zunionstore(): Unit = {
    val key = "zset-key"
    val key2 = "zset-key2"
    val setKey = "set-key"
    val dest = "dest-zset-key"
    val result = connect { client =>
      client.del(key)
      client.del(key2)
      client.del(dest)
      client.del(setKey)
      client.zadd(key, (1, "k1"), (2, "k2"), (10, "k10"))
      client.zadd(key2, (5, "k1"), (6, "k2"), (8, "k8"))
      // Set elements's score will be regarded as 1
      client.sadd(setKey, "k1", "k100")

      client.zunionstore(dest, key, Seq(key2, setKey), SUM)
      client.zrangeWithscores[String](dest, 0, -1)
    }

    assertEqual(result.size, 5)
    assertEqual(result.head._1, "k100")
    assertEqual(result.head._2, 1)
    assertEqual(result(1)._1, "k1")
    assertEqual(result(1)._2, 7)
    // "k2" and "k8" now both have same score: 8
    assertEqual(result(2)._2, 8)
  }
}
