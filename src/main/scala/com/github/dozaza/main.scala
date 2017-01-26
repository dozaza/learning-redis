package com.github.dozaza

import com.github.dozaza.test.{CommandTest, PingTest, SimpleTest}


object main {

  def main(args: Array[String]): Unit = {
    PingTest.test()
    SimpleTest.test()
    CommandTest.test()
  }



}
