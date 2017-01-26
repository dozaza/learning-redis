
package com.github.dozaza.test

import com.github.dozaza.simple._

object SimpleTest {

  def test(): Unit = {
    testStringOperation()
    testListOperation()
    testSetOperation()
    testHashOperation()
    testZSetOperation()
  }

  private def testStringOperation(): Unit = {
    StringOperation.get()
    StringOperation.set()
    StringOperation.delete()
  }

  private def testListOperation(): Unit = {
    ListOperation.rpush()
    ListOperation.lrange()
    ListOperation.lindex()
    ListOperation.lpop()
  }

  private def testSetOperation(): Unit = {
    SetOperation.sadd()
    SetOperation.sismember()
    SetOperation.smembers()
    SetOperation.srem()
  }

  private def testHashOperation(): Unit = {
    HashOperation.hset()
    HashOperation.hget()
    HashOperation.hgetall()
    HashOperation.hdel()
  }

  private def testZSetOperation(): Unit = {
    ZSetOperation.zadd()
    ZSetOperation.zrange()
    ZSetOperation.zrangescore()
    ZSetOperation.zrem()
  }

}
