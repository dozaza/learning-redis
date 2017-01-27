
package com.github.dozaza.command

import com.github.dozaza.connect.dsl._
import com.github.dozaza.test.TestModule

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object ListCommand extends TestModule {

  def lrange(): Unit = {
    val key = "test-list"
    val list = connect{ client =>
      client.del(key)
      client.lpush(key, "element1", "element2", "element3")
      client.lrange[String](key, 1, 2)
    }
    assertEqual(list.size, 2)
    assertEqual(list.head, "element2")
    assertEqual(list.last, "element1")
  }

  def ltrim(): Unit = {
    val key = "test-list"
    val list = connect{ client =>
      client.del(key)
      client.rpush(key, "element1", "element2", "element3")
      client.ltrim(key, 1, -1)
      client.lrange[String](key, 0, -1)
    }
    assertEqual(list.size, 2)
    assertEqual(list.head, "element2")
    assertEqual(list.last, "element3")
  }

  def blpop(): Unit = {
    val key = "test-list"
    connect{ client =>
      client.del(key)
      client.rpush(key, "element1", "element2", "element3")
      client.lrange[String](key, 0, -1)
    }
    // Wait 1 minute until an element comes into list
    connectWithBlock{ blockClient =>
      blockClient.blpop(Seq(key), 1 minutes)
    }
    val list = connect{ client =>
      client.lrange[String](key, 0, -1)
    }
    assertEqual(list.size, 2)
    assertEqual(list.head, "element2")
  }

  def brpop(): Unit = {
    val key = "test-list"
    connect{ client =>
      client.del(key)
      client.rpush(key, "element1", "element2", "element3")
      client.lrange[String](key, 0, -1)
    }
    // Wait 1 minute until an element comes into list
    connectWithBlock{ blockClient =>
      blockClient.brpop(Seq(key), 1 minutes)
    }
    val list = connect{ client =>
      client.lrange[String](key, 0, -1)
    }
    assertEqual(list.size, 2)
    assertEqual(list.head, "element1")
  }

  def rpoplpush(): Unit = {
    val key = "test-list"
    val key2 = "test-list2"
    val (list1, list2) = connect{ client =>
      client.del(key)
      client.del(key2)
      client.rpush(key, "element1", "element2", "element3")
      client.rpoplpush(key, key2)
      for {
        l1 <- client.lrange[String](key, 0, -1)
        l2 <- client.lrange[String](key2, 0, -1)
      } yield {
        (l1, l2)
      }
    }

    assertEqual(list1.size, 2)
    assertEqual(list1.head, "element1")
    assertEqual(list2.size, 1)
    assertEqual(list2.head, "element3")
  }

  def brpoplpush(): Unit = {
    val key = "test-list"
    val key2 = "test-list2"
    connect{ client =>
      client.del(key)
      client.del(key2)
      client.rpush(key, "element1", "element2", "element3")
      client.lrange[String](key, 0, -1)
    }
    // Wait 1 minute until an element comes into list
    connectWithBlock{ blockClient =>
      blockClient.brpoplpush(key, key2)
    }
    val (list1, list2) = connect{ client =>
      for {
        l1 <- client.lrange[String](key, 0, -1)
        l2 <- client.lrange[String](key2, 0, -1)
      } yield {
        (l1, l2)
      }
    }

    assertEqual(list1.size, 2)
    assertEqual(list1.head, "element1")
    assertEqual(list2.size, 1)
    assertEqual(list2.head, "element3")
  }
}
