
package com.github.dozaza.test

import com.github.dozaza.command.{ListCommand, StringCommand}

object CommandTest {

  def test(): Unit = {
    testStringCommand()
    testListCommand()
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
}
