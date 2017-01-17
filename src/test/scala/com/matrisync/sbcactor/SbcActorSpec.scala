package com.matrisync.sbcactor

import java.util.Date

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit._
import akka.util.Timeout
import com.codahale.metrics.{MetricRegistry, Timer}
import com.matrisync.sbcactor.SbcActorProtocol.SbcActorRequest._
import com.matrisync.sbcactor.SbcActorProtocol.SbcActorResponse.MessageNotRecognized
import com.typesafe.config.{Config, ConfigFactory}
import org.scalatest._
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}

import scala.collection.JavaConverters._
import scala.concurrent.duration._

// class SbcActorSpecSuite extends Suites(new SbcActorSpec)

object TestKitUsageSpec {
  // test-specific configuration
  val localConfig =
    """
 akka {
   loglevel = "DEBUG"
   stdout-loglevel = "DEBUG"
   actor.debug.receive = on
#   actor {
#     provider = remote
#   }
#   remote {
#     enabled-transports = ["akka.remote.netty.tcp"]
#     netty.tcp {
#       hostname = "127.0.0.1"
#       port = 2552
#     }
#   }
 }
   com.matrisync.SbcActor {
     configName = "SbcActorSpec config"
     foo = bar
   }

    """
  val config = ConfigFactory.parseString(localConfig).resolve
  //  val config = ConfigFactory.parseString(localConfig).withFallback(ConfigFactory.defaultApplication()).resolve
  //  val config = ConfigFactory.defaultApplication.resolve
}

class SbcActorSpec extends
  TestKit(ActorSystem("SbcActorSpec", TestKitUsageSpec.config))
  with WordSpecLike
  with ScalaFutures
  with MustMatchers
  with BeforeAndAfterAll
  with IntegrationPatience {

  override def afterAll() {
    system.terminate
  }

  val actor = TestActorRef[SbcActor]("SbcActorTestTestActor")
  implicit val timeout = Timeout(5 seconds)

  "SbcActor" should {

    "complain about unrecognized messages" in {
      whenReady(actor ? "You can't touch this") {
        r => {
          r mustBe MessageNotRecognized
        }
      }
    }

    "show the config" in {
      whenReady(actor ? ShowConfig("com.matrisync")) {
        r => {
          info(s"ShowConfig returned: ${r.toString}")
          assert(r.isInstanceOf[Config])
          info(s"com.matrisync.SbcActor.foo: ${r.asInstanceOf[Config].getString("SbcActor.foo")}  ")
        }
      }
    }

    "Do an eval" in {
      whenReady(actor ? DoEval("2 + 2")) {
        r => {
          info(s"Eval: ${r.toString}")
          r mustBe 4
        }
      }
    }

    "remember things" in {

      whenReady(actor ? Remember("foo","bar"))
      {
        r => {
          info(s"Remember values returned: $r")
          whenReady(actor ? ShowMemory) {
            r => info(s"ShowMemory returned: $r")
          }
        }
      }
    }

    "return metrics" in {
      whenReady(actor ? Metrics) {
        r => {
          info(s"Metrics: ${r.asInstanceOf[MetricRegistry].toString}")
          r.asInstanceOf[MetricRegistry].getMetrics.keySet.size must be > (1)
        }
      }
    }



  }
}



