package com.github.dozaza.simple

import com.github.dozaza.connect.dsl._
import com.github.dozaza.test.TestModule

object StringOperation extends TestModule {

  def get(): Unit = {
    val key = "name"
    val resultOpt = connect[Option[String]] { client =>
      client.get[String](key)
    }

    println(resultOpt)
  }

  def set(): Unit = {
    val key = "name"
    val value = "dozaza"

    val flag = connect[Boolean] { client => client.set(key, value) }
    if (!flag) {
      throw new RuntimeException(s"Unable to set key: $key with value: $value")
    }

    val resultOpt = connect[Option[String]] { client =>
      client.get[String](key)
    }
    assertEqual(resultOpt, Some(value))
  }

  def delete(): Unit = {
    val key = "name"
    val flag = connect[Long]{ client => client.del(key) }
    if (flag > 0) {
      val resultOpt = connect[Option[String]] { client =>
        client.get[String](key)
      }
      assertEqual(resultOpt, None)
    }
  }

}
