import com.typesafe.sbt.SbtMultiJvm

name := "sbc-actor"

version := "1.0"

scalaVersion := "2.11.8"

val akkaVersion = "2.4.14"

lazy val sbcactor: Project = project.in(file(".")).enablePlugins(JavaAppPackaging)
mainClass in(Compile, packageBin) := Some("com.matrisync.sbcactor.SbcActorMain")
mainClass in(Compile, run) := Some("com.matrisync.sbcactor.SbcActorMain")


// akka herself
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-contrib" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % "10.0.1",
  "com.typesafe.akka" %% "akka-http-core" % "10.0.1",
  "com.typesafe.akka" %% "akka-multi-node-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "org.scalatest" %% "scalatest" % "2.2.4"
)

// metrics
// we'll be on 3.5.5._a2.3 until Scala 2.12 ships
// Until then during build you will see:
//  [warn] There may be incompatibilities among your library dependencies.
//  [warn] Here are some of the libraries that were evicted:
//  [warn]  * com.typesafe.akka:akka-actor_2.11:2.3.15 -> 2.4.11
// "nl.grons" %% "metrics-scala" % "3.5.5_a2.4",
libraryDependencies ++= Seq(
  "nl.grons" %% "metrics-scala" % "3.5.5_a2.3"
  //  "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.3",
  //  "com.fasterxml.jackson.core" % "jackson-core" % "2.8.3"
)

// eval
libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler" % "2.11.8",
  "org.scala-lang" % "scala-library" % "2.11.8",
  "org.scala-lang" % "scala-reflect" % "2.11.8",
  "org.scala-lang.modules" % "scala-parser-combinators_2.11" % "1.0.2",
  "com.twitter" %% "util-eval" % "6.32.0",
  "com.twitter" %% "chill-all" % "0.8.0",
  "com.twitter" %% "chill-akka" % "0.8.0"
)

// motherhood, apple pie
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test")

libraryDependencies += "org.json4s" % "json4s-native_2.11" % "3.5.0"
libraryDependencies += "org.json4s" % "json4s-ext_2.11" % "3.5.0"
    