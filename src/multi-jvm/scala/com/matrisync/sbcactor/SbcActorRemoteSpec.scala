package com.matrisync.sbcactor

import akka.actor.{Actor, ActorSystem, Identify, Props}
import akka.pattern.ask
import akka.remote.ContainerFormats.Identify
import akka.remote.testkit._
import akka.testkit.{TestActorRef, TestKit}
import akka.util.Timeout
import com.matrisync.sbcactor.SbcActorProtocol.SbcActorRequest.{DoEval, ShowConfig}
import com.typesafe.config.{Config, ConfigFactory}
import org.scalatest._
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}

import scala.concurrent.Await
import scala.concurrent.duration._

// class SbcActorSpecSuite extends Suites(new SbcActorSpec)
object RemoteTestConfig {
  // test-specific configuration
  val localConfig =
    """
      akka {
        actor {
        provider = remote
         serializers {
           java = "akka.serialization.JavaSerializer"
           proto = "akka.remote.serialization.ProtobufSerializer"
         }
       serialization-bindings {
            "java.lang.String" = java
            "com.google.protobuf.Message" = proto
          }
        }
        remote {
          enabled-transports = ["akka.remote.netty.tcp"]
          netty.tcp {
            hostname = "faithhold.dyndns.org"
            port = 2553
          }
          }
          }
   com.matrisync.SbcActor {
     foo = bar
   }
    """
   val config = ConfigFactory.parseString(localConfig).resolve
  // val config = ConfigFactory.defaultApplication.resolve
}

class SbcActorRemoteSpec extends TestKit(ActorSystem("SbcActorSpec", RemoteTestConfig.config))
  with WordSpecLike
  with ScalaFutures
  with MustMatchers
  with BeforeAndAfterAll
  with IntegrationPatience {

  implicit val timeout = Timeout(10 seconds)

  override def afterAll() {
    system.terminate
  }

   val SbcActorTestActor = system.actorSelection("akka.tcp://Main@127.0.0.1:2553/user/app")
  whenReady(SbcActorTestActor ? akka.actor.Identify("whodat")) {
    r => {
      info(s"Identify: ${r}  ")
    }
  }

  "RemoteSbcActor" should {

//    "complain about unrecognized messages" in {
//      whenReady(SbcActorTestActor ? "You can't touch this") {
//        r => {
//          r mustBe MessageNotRecognized
//        }
//      }
//    }

    "show the config" in {
      whenReady(SbcActorTestActor ? ShowConfig) {
        r => {
          info(s"ShowConfig: ${r}  ")
        }
      }
    }
  }

  "Do an eval" in {
    whenReady(SbcActorTestActor ? DoEval("2 + 2")) {
      r => {
        info(s"Eval: ${r.toString}")
      }
    }
  }

//  "return metrics" in {
//    whenReady(SbcActorTestActor ? Metrics) {
//      r => {
//       // info(s"Metrics: ${r} ")
//        r.toString.length mustBe > (50)
//      }
//    }
//  }
}



