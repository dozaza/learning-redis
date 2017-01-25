package com.github.dozaza

import com.github.dozaza.base.StringOperation
import redis.RedisClient

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


object main {

  def main(args: Array[String]): Unit = {
    StringOperation.get()
    StringOperation.set()
    StringOperation.delete()
  }

  private def testPing(): Unit = {
    implicit val akkaSystem = akka.actor.ActorSystem()

    val redis = RedisClient()

    val futurePong = redis.ping()
    println("Ping sent!")
    futurePong.map(pong => {
      println(s"Redis replied with a $pong")
    })
    Await.result(futurePong, 5 seconds)

    akkaSystem.shutdown()
  }
}
