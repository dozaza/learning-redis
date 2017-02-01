
package com.github.dozaza.test

import com.github.dozaza.logging.Logging
import redis.RedisClient
import com.github.dozaza.redis.dsl._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object PingTest extends TestModule with Logging {

  def test(): Unit = {
    testPing()
    log.info("Ping test finished")
  }

  private def testPing(): Unit = {
    val redis = RedisClient()

    val futurePong = redis.ping()
    futurePong.map(pong => {
      assertEqual(pong, "PONG")
    })
    Await.result(futurePong, 5 seconds)
  }
}
