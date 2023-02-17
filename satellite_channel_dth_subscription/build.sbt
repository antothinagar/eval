name := "satellite_channel_dth_subscription"
 
version := "1.0" 
      
lazy val `satellite_channel_dth_subscription` = (project in file(".")).enablePlugins(PlayScala)

      
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
      
scalaVersion := "2.13.5"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice, filters,
  "mysql" % "mysql-connector-java" % "5.1.36",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.typesafe.slick" %% "slick" % "3.3.2",
  "org.scalatest" %% "scalatest" % "3.1.1" % "test",
  "com.h2database" % "h2" % "1.4.187" % "test",
  "org.scalatest" %% "scalatest" % "3.1.1" % Test,
  "com.google.inject" % "guice" % "5.0.1",
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test,
  "org.mockito" % "mockito-core" % "1.10.19" % Test

)
      