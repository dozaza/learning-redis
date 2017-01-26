
package com.github.dozaza.test

import com.github.dozaza.command.StringCommand

object CommandTest {

  def test(): Unit = {
    testStringCommand()
  }

  private def testStringCommand(): Unit = {
    StringCommand.incr()
    StringCommand.decr()
    StringCommand.incrbyfloat()
  }

}
