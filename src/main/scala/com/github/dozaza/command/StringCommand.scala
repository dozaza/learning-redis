
package com.github.dozaza.command

import com.github.dozaza.connect.dsl._
import com.github.dozaza.test.TestModule

object StringCommand extends TestModule {

  def incr(): Unit = {
    val key = "test"
    val value = connect[Long]{ client =>
      client.del(key)
      client.incr(key)
    }
    assertEqual(value, 1)
    val value2 = connect[Long]{ client =>
      client.del(key)
      client.incrby(key, 10)
    }
    assertEqual(value2, 10)
  }

  def decr(): Unit = {
    val key = "test"
    val value = connect[Long]{ client =>
      client.del(key)
      client.decr(key)
    }
    assertEqual(value, -1)
    val value2 = connect[Long]{ client =>
      client.del(key)
      client.decrby(key, 10)
    }
    assertEqual(value2, -10)
  }

  def incrbyfloat(): Unit = {
    val key = "test"
    val value = connect[Option[Double]]{ client =>
      client.del(key)
      client.incrbyfloat(key, 2.2)
    }
    assertEqual(value, Some(2.2d))
  }
}
