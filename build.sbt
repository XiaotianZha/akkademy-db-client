
name := "akkademy-db-client"

version := "0.1"

scalaVersion := "2.12.2"

lazy val akkaVersion = "2.5.12"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "com.akkademy-db"   %% "leanakka" % "0.3-SNAPSHOT",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)