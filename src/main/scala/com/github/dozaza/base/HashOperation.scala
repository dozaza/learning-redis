
package com.github.dozaza.base

import com.github.dozaza.connect.dsl._
import com.github.dozaza.test.TestModule

object HashOperation extends TestModule {

  def hset(): Unit = {
    val key = "test-hash"
    val flag = connect[Boolean]{ client =>
      client.del(key)
      client.hset(key, "dozaza", "coder")
    }
    val flag2 = connect[Boolean]{ client =>
      client.hset(key, "dozaza", "programmer")
    }
    assertEqual(flag, true)
    assertEqual(flag2, false)
  }

  def hget(): Unit = {
    val key = "test-hash"
    val flag = connect[Boolean]{ client =>
      client.del(key)
      client.hset(key, "dozaza", "coder")
    }
    val resultOpt = connect[Option[String]]{ client =>
      client.hget[String]("test-hash", "dozaza")
    }
    assertEqual(resultOpt, Some("coder"))
  }

  def hgetall(): Unit = {
    val key = "test-hash"
    val flag = connect[Boolean]{ client =>
      client.del(key)
      client.hset(key, "dozaza", "coder")
      client.hset(key, "catye", "designer")
    }
    val map = connect[Map[String, String]]{ client =>
      client.hgetall[String](key)
    }
    assertEqual(map.size, 2)
    assertEqual(map.get("dozaza"), Some("coder"))
  }

  def hdel(): Unit = {
    val key = "test-hash"
    val flag = connect[Boolean]{ client =>
      client.del(key)
      client.hset(key, "dozaza", "coder")
    }
    val resultOpt = connect[Option[String]]{ client =>
      client.hget[String](key, "dozaza")
    }
    assertEqual(resultOpt, Some("coder"))
    val resultOpt2 = connect[Option[String]]{ client =>
      client.hdel(key, "dozaza")
      client.hget[String](key, "dozaza")
    }
    assertEqual(resultOpt2, None)
  }

}
