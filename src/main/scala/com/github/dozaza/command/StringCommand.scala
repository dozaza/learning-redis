
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

  def incrByFloat(): Unit = {
    val key = "test"
    val value = connect[Option[Double]]{ client =>
      client.del(key)
      client.incrbyfloat(key, 2.2)
    }
    assertEqual(value, Some(2.2d))
  }

  def append(): Unit = {
    val key = "test"
    val length = connect[Long]{ client =>
      client.del(key)
      client.set(key, "dozaza")
      client.append(key, "_coder")
    }
    assertEqual(length, 12)
  }

  def getRange(): Unit = {
    val key = "test"
    val value = connect[Option[String]]{ client =>
      client.del(key)
      client.set(key, "dozaza_coder")
      client.getrange[String](key, 7, 11)
    }
    assertEqual(value, Some("coder"))

    val value2 = connect[Option[String]]{ client =>
      client.del(key)
      client.set(key, "dozaza_coder")
      client.getrange[String](key, 12, 13)
    }
    assertEqual(value2, Some(""))
  }

  def setRange(): Unit = {
    val key = "test"
    val value = connect[Option[String]]{ client =>
      client.del(key)
      client.set(key, "dozaza_coder")
      client.setrange(key, 7, "CODER")
      client.get[String](key)
    }
    assertEqual(value, Some("dozaza_CODER"))

    val value2 = connect[Option[String]]{ client =>
      client.del(key)
      client.set(key, "dozaza")
      client.setrange(key, 6, "_coder")
      client.get[String](key)
    }
    assertEqual(value2, Some("dozaza_coder"))
  }

}
