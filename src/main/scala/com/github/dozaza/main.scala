package com.github.dozaza

import com.github.dozaza.pubsub.Pubsub
import com.github.dozaza.test.{CommandTest, PingTest, SimpleTest}
import com.github.dozaza.redis.dsl._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object main {

  def main(args: Array[String]): Unit = {
    PingTest.test()
    SimpleTest.test()
    CommandTest.test()
    Pubsub.start()

    akkaSystem.scheduler.scheduleOnce(10 seconds)(akkaSystem.shutdown())
  }



}
