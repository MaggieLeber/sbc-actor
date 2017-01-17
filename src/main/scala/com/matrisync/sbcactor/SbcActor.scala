package com.matrisync.sbcactor

import akka.actor.{Actor, ActorLogging, Props, Terminated}
import akka.pattern.ask
import akka.contrib.pattern.ReceivePipeline
import akka.event.LoggingReceive
import akka.util.Timeout
import com.matrisync.sbcactor.SbcActorProtocol.SbcActorRequest.{DoEval, Remember, ShowConfig, ShowMemory}
import com.matrisync.sbcactor.SbcActorProtocol.SbcActorResponse.{Done, MessageNotRecognized}
import com.typesafe.config.{Config, ConfigException}
import nl.grons.metrics.scala._
import com.twitter.util.Eval

import scala.concurrent.Await
import scala.concurrent.duration._


trait SbcActorBehavior extends Actor {

  import akka.event.{Logging, LoggingReceive}
  import akka.pattern.{CircuitBreaker, pipe}
  import context.dispatcher

  import scala.concurrent.duration._

  val log = Logging(context.system, this)
  var memory = Map[String, Any]()

  def receive = LoggingReceive {
    case ShowConfig(path: String)  => sender ! context.system.settings.config.getConfig(path)
    case DoEval(s)     => sender ! (new Eval()(s))
    case Remember(k,v) => {
      memory = memory.updated(k,v)
      sender ! Done
    }
    case ShowMemory => sender ! memory
    case wtf         => sender ! MessageNotRecognized
  }

  val config: Config = context.system.settings.config
  val breakerConfig = "com.matrisync.services.breaker."
  val maxFailures = try config.getInt(s"$breakerConfig.maxFailures") catch {
    case e: ConfigException => 5
  }
  val callTimeout = try config.getInt(s"$breakerConfig.callTimeout") catch {
    case e: ConfigException => 5
  }
  val resetTimeout = try config.getInt(s"$breakerConfig.resetTimeout") catch {
    case e: ConfigException => 1
  }

  val breaker =
    new CircuitBreaker(
      context.system.scheduler,
      maxFailures = maxFailures,
      callTimeout = callTimeout.seconds,
      resetTimeout = resetTimeout.minute).onOpen(notifyMeOnOpen())

  def notifyMeOnOpen(): Unit = log.warning(s"${context.self.path}: CircuitBreaker is now open, and will not close for $resetTimeout minutes")

}

class SbcActor
  extends SbcActorBehavior
    with ActorInstrumentedLifeCycle
    with ReceiveCounterActor
    with ReceiveTimerActor
    with ReceiveExceptionMeterActor
    with ReceivePipeline
    with InstrumentedActor
{
  // log.info(context.system.settings.config.atPath("akka").toString)
  log.info(s"sbcactorActor started: ${self.path}")
}




