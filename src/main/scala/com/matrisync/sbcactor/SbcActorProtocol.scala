package com.matrisync.sbcactor

object SbcActorProtocol {

  sealed trait SbcActorProtocol extends Serializable

  sealed trait SbcActorRequest extends SbcActorProtocol

  object SbcActorRequest {

    case object Ping extends SbcActorRequest

    case class ShowConfig(path : String) extends SbcActorRequest

    case class DoEval(s: String) extends SbcActorRequest

    case object Metrics extends SbcActorRequest

    case class Remember(key: String, value: Any) extends SbcActorRequest

    case object ShowMemory extends SbcActorRequest

  }

  sealed trait SbcActorResponse extends SbcActorProtocol

  object SbcActorResponse {

    case object MessageNotRecognized extends SbcActorResponse

    case object ServiceNotImplemented extends SbcActorResponse

    case object Done extends SbcActorResponse

  }

}
