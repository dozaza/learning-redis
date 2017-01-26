
package com.github.dozaza.simple

import com.github.dozaza.connect.dsl.connect
import com.github.dozaza.test.TestModule

object SetOperation extends TestModule {

  def sadd(): Unit = {
    val key = "test_set"

    val flag = connect[Long]{ client =>
      client.del(key)
      client.sadd(key, "element1", "element2", "element1")
    }

    assertEqual(flag, 2)
  }

  def smembers(): Unit = {
    val key = "test_set"

    val seq = connect[Seq[String]]{ client =>
      client.del(key)
      client.sadd(key, "element1", "element2", "element1")
      client.smembers[String](key)
    }

    assertEqual(seq.size, 2)
  }

  def sismember(): Unit = {
    val key = "test_set"

    val flag = connect[Long]{ client =>
      client.del(key)
      client.sadd(key, "element1", "element2", "element1")
    }
    if (flag > 0) {
      val flag = connect[Boolean]{client => client.sismember(key, "element1")}
      val flag2 = connect[Boolean]{client => client.sismember(key, "not_exist")}
      assertEqual(flag, true)
      assertEqual(flag2, false)
    }
  }

  def srem(): Unit = {
    val key = "test_set"

    val flag = connect[Long]{ client =>
      client.del(key)
      client.sadd(key, "element1", "element2", "element1")
    }
    if (flag > 0) {
      val flag = connect[Long]{client => client.srem(key, "element1")}
      val flag2 = connect[Long]{client => client.srem(key, "element1")}
      assertEqual(flag, 1)
      assertEqual(flag2, 0)
    }
  }
}
