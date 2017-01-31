
package com.github.dozaza.test

import com.github.dozaza.command.{ListCommand, SetCommand, StringCommand}

object CommandTest {

  def test(): Unit = {
    testStringCommand()
    testListCommand()
    testSetCommand()
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
  }
}
