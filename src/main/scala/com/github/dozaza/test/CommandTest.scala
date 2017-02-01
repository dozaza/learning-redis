
package com.github.dozaza.test

import com.github.dozaza.command._
import com.github.dozaza.logging.Logging

object CommandTest extends Logging {

  def test(): Unit = {
    testStringCommand()
    testListCommand()
    testSetCommand()
    testHashCommand()
    testZSetCommand()
    log.info("Command test finished")

  }

  private def testStringCommand(): Unit = {
    StringCommand.incr()
    StringCommand.decr()
    StringCommand.incrByFloat()
    StringCommand.append()
    StringCommand.getRange()
    StringCommand.setRange()
  }

  private def testListCommand(): Unit = {
    ListCommand.lrange()
    ListCommand.ltrim()
    ListCommand.blpop()
    ListCommand.brpop()
    ListCommand.rpoplpush()
    ListCommand.brpoplpush()
  }

  private def testSetCommand(): Unit = {
    SetCommand.sadd()
    SetCommand.sismember()
    SetCommand.scard()
    SetCommand.smembers()
    SetCommand.spop()
    SetCommand.smove()
    SetCommand.sdiff()
    SetCommand.sdiffstore()
    SetCommand.sinter()
    SetCommand.sinterstore()
    SetCommand.sunion()
    SetCommand.sunionstore()
  }

  private def testHashCommand(): Unit = {
    HashCommand.hmgetAndHmset()
    HashCommand.hdel()
    HashCommand.hexists()
    HashCommand.hkeys()
    HashCommand.hvals()
    HashCommand.hgetall()
    HashCommand.hincrby()
    HashCommand.hincrbyfloat()
  }

  private def testZSetCommand(): Unit = {
    ZSetCommand.zadd()
    ZSetCommand.zrem()
    ZSetCommand.zcard()
    ZSetCommand.zincrby()
    ZSetCommand.zcount()
    ZSetCommand.zrank()
    ZSetCommand.zscore()
    ZSetCommand.zrange()
    ZSetCommand.zrevrank()
    ZSetCommand.zrevrange()
    ZSetCommand.zrangebyscore()
    ZSetCommand.zrevrangebyscore()
    ZSetCommand.zremrangebyrank()
    ZSetCommand.zremrangebyscore()
    ZSetCommand.zinterstore()
    ZSetCommand.zunionstore()
  }
}
