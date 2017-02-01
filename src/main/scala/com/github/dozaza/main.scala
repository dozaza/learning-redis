package com.github.dozaza

import com.github.dozaza.test.{CommandTest, PingTest, SimpleTest}

import com.github.dozaza.redis.dsl._

object main {

  def main(args: Array[String]): Unit = {
    PingTest.test()
    SimpleTest.test()
    CommandTest.test()

    actorSystem.shutdown()
  }



}
