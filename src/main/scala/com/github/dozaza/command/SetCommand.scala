
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

  /**
    * Return the number of elements in a set
    */
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

  /**
    * Pop an element randomly
    */
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

  /**
    * Return elements exists in first key, not in the rest
    */
  def sdiff(): Unit = {
    val key = "set-key"
    val key2 = "set-key2"
    val key3 = "set-key3"
    val diff = connect { client =>
      client.del(key)
      client.del(key2)
      client.del(key3)

      client.sadd(key, "element1", "element2", "element3", "element4", "element5", "element6")
      client.sadd(key2, "element2", "element4")
      client.sadd(key3, "element1", "element5")

      client.sdiff[String](key, key2, key3)
    }

    assertEqual(diff.size, 2)
    assertEqual(diff.contains("element3"), true)
    assertEqual(diff.contains("element6"), true)
  }

  /**
    * Store elements exists in first key, not in the rest into a another set
    */
  def sdiffstore(): Unit = {
    val key = "set-key"
    val key2 = "set-key2"
    val key3 = "set-key3"
    val dest = "dest-set-key"
    val diff = connect { client =>
      client.del(key)
      client.del(key2)
      client.del(key3)
      client.del(dest)

      client.sadd(key, "element1", "element2", "element3", "element4", "element5", "element6")
      client.sadd(key2, "element2", "element4")
      client.sadd(key3, "element1", "element5")

      client.sdiffstore(dest, key, key2, key3)
      client.smembers[String](dest)
    }

    assertEqual(diff.size, 2)
    assertEqual(diff.contains("element3"), true)
    assertEqual(diff.contains("element6"), true)
  }

  /**
    * Return elements exists in every set in the same time
    */
  def sinter(): Unit = {
    val key = "set-key"
    val key2 = "set-key2"
    val inter = connect { client =>
      client.del(key)
      client.del(key2)

      client.sadd(key, "element1", "element2", "element3", "element4", "element5", "element6")
      client.sadd(key2, "element2", "element4")

      client.sinter[String](key, key2)
    }

    assertEqual(inter.size, 2)
    assertEqual(inter.contains("element2"), true)
    assertEqual(inter.contains("element4"), true)
  }

  /**
    * Store elements exists in every set in the same time into a another set
    */
  def sinterstore(): Unit = {
    val key = "set-key"
    val key2 = "set-key2"
    val dest = "dest-set-key"
    val inter = connect { client =>
      client.del(key)
      client.del(key2)
      client.del(dest)

      client.sadd(key, "element1", "element2", "element3", "element4", "element5", "element6")
      client.sadd(key2, "element2", "element4")

      client.sinterstore(dest, key, key2)
      client.smembers[String](dest)
    }

    assertEqual(inter.size, 2)
    assertEqual(inter.contains("element2"), true)
    assertEqual(inter.contains("element4"), true)
  }

  def sunion(): Unit = {
    val key = "set-key"
    val key2 = "set-key2"

    val unioned = connect { client =>
      client.del(key)
      client.del(key2)

      client.sadd(key, "element1", "element2", "element3")
      client.sadd(key2, "element2", "element4")

      client.sunion[String](key, key2)
    }

    assertEqual(unioned.size, 4)
  }

  def sunionstore(): Unit = {
    val key = "set-key"
    val key2 = "set-key2"
    val dest = "dest-set-key"

    val unioned = connect { client =>
      client.del(key)
      client.del(key2)

      client.sadd(key, "element1", "element2", "element3")
      client.sadd(key2, "element2", "element4")

      client.sunionstore(dest, key, key2)
      client.smembers[String](dest)
    }

    assertEqual(unioned.size, 4)
  }
}
