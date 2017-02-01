name := "redis-com.github.dozaza.test"

version := "0.0.x"

libraryDependencies ++= {
  Seq(
    "com.github.etaty" %% "rediscala" % "1.7.0",
    "org.slf4j" % "slf4j-api" % "1.7.2",
    "ch.qos.logback" % "logback-classic" % "1.1.2",
    "com.typesafe" % "config" % "1.3.1",
    "com.typesafe.akka" %% "akka-actor" % "2.3.6",
    "com.typesafe.akka" %% "akka-slf4j" % "2.3.6"
  )
}
