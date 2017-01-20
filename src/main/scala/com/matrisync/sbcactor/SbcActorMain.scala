package com.matrisync.sbcactor

object SbcActorMain extends App {
  println(System.getProperties.toString)
  akka.Main.main(Array("com.matrisync.sbcactor.SbcActor"))
}
