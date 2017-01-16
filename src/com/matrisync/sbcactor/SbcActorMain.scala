package com.matrisync.sbc-actor
object SbcActorMain extends App {
  println(System.getProperties.toString)
  akka.Main.main(Array("com.matrisync.SbcActor"))
}
