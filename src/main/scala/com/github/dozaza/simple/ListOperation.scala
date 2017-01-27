
package com.github.dozaza.simple

import com.github.dozaza.redis.dsl._
import com.github.dozaza.test.TestModule

object ListOperation extends TestModule {

  def rpush(): Unit = {
    val key = "list_test_key"
    val flag = connect[Long]{ client =>
      client.del(key)
      client.rpush(key, "element1", "element2", "element3")
    }
    assertEqual(flag, 3)
  }

  def lrange(): Unit = {
    val key = "list_test_key"
    val seq = connect[Seq[String]]{ client =>
      client.del(key)
      client.rpush(key, "element1", "element2", "element3")
      client.lrange[String](key, 1, 2)
    }
    assertEqual(seq.size, 2)
    assertEqual(seq.head, "element2")
  }

  def lindex(): Unit = {
    val key = "list_test_key"
    val result = connect[Option[String]]{ client =>
      client.del(key)
      client.rpush(key, "element1", "element2", "element3")
      client.lindex[String](key, 1)
    }
    assertEqual(result, Some("element2"))
  }

  def lpop(): Unit = {
    val key = "list_test_key"
    val result = connect[Option[String]]{ client =>
      client.del(key)
      client.rpush(key, "element1", "element2", "element3")
      client.lpop[String](key)
    }
    assertEqual(result, Some("element1"))
  }

}
