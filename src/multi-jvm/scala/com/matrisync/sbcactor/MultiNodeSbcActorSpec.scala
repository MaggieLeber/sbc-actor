package com.matrisync.sbcactor

import java.io.{ByteArrayOutputStream, PrintStream}
import java.util
import java.util.concurrent.TimeUnit

import akka.actor
import akka.actor.Props
import akka.remote.testkit.{MultiNodeConfig, MultiNodeSpec}
import akka.testkit.ImplicitSender
import akka.util.Timeout
import com.codahale.metrics._
import com.typesafe.config.{Config, ConfigFactory}
import org.scalatest._
import com.matrisync.sbcactor.SbcActorProtocol.SbcActorRequest._
import com.matrisync.sbcactor.SbcActorProtocol.SbcActorResponse.MessageNotRecognized
import com.matrisync.sbcactor.MultiNodeSampleConfig._
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import akka.pattern.ask

import scala.collection.mutable

object MultiNodeSbcActorConfig extends MultiNodeConfig {
  val node1 = role("node1")
  val node2 = role("node2")
  val ourCommonConfig =  // this config is common to all nodes in the test
    """
      |akka {
      |   # loglevel = "DEBUG"
      |   # stdout-loglevel = "DEBUG"
      |   actor.debug.receive = on
      |   actor {
      |     provider = remote
      |         serializers {
      |          kryo = "com.twitter.chill.akka.AkkaSerializer"
      |           java = "akka.serialization.JavaSerializer"
      |     #      proto = "akka.remote.serialization.ProtobufSerializer"
      |         }
      |     serialization-bindings {
      |           "java.lang.String" = java
      |      #     "com.google.protobuf.Message" = proto
      |      #     "com.matrisync.sbcactor.SbcActorProtocol$SbcActorProtocol" = kryo
      |          "com.codahale.metrics.MetricRegistry" = kryo

      |          # Note: "com.typesafe.config.impl.SimpleConfig" = kryo can't serialize this out-of-the-box
      |          # "com.esotericsoftware.kryo.KryoException: java.lang.UnsupportedOperationException:"
      |          #  "ConfigObject is immutable, you can't call Map.put"
      |         }
      |       }
      |   }
      |  com.matrisync.sbcactor {
      |    configName = "MultiNodeSbcActorSpec common"
      |    foo = bar
      |  }
    """.stripMargin
  // debugConfig(true)
  commonConfig(ConfigFactory.parseString(ourCommonConfig))
}

class MultiNodeSbcActorSpecMultiJvmNode1 extends MultiNodeSbcActorSpec
class MultiNodeSbcActorSpecMultiJvmNode2 extends MultiNodeSbcActorSpec

class MultiNodeSbcActorSpec extends MultiNodeSpec(MultiNodeSbcActorConfig)
  with STMultiNodeSpec
  with ImplicitSender
  with ScalaFutures
  with IntegrationPatience
{
  def initialParticipants = roles.size

  "MultiNode SbcActor Test" must {



    "wait for all nodes to enter a barrier" in {
      enterBarrier("startup")
    }

    "send to and receive from a remote node" in {
      runOn(node1) {

        enterBarrier("deployed")
        val sbcactor = system.actorSelection(node(node2) / "user" / "sbcactor")
        info("I'm probing the sbcactor")

        // akka-testkit-tyle test
        sbcactor ! "You can't touch this"
        import scala.concurrent.duration._
        expectMsg(10.seconds, MessageNotRecognized)

        // scalatest-style test
        implicit val akkaAskTimeout = Timeout(5 seconds)
        whenReady(sbcactor ? ShowConfig("com.matrisync")) {
          r => {
            assert(r.isInstanceOf[Config])
            info(s"Reported configuration is: ${r.asInstanceOf[Config].getString("sbcactor.configName")}  ")
            info(s"com.matrisync.sbcactor.foo: ${r.asInstanceOf[Config].getString("sbcactor.foo")}  ")
          }
        }

          whenReady(sbcactor ? DoEval("2 + 2")) {
            r => {
              info(s"Eval: ${r.toString}")
              r should be (4)
            }
          }

        whenReady(sbcactor ?
            Remember("rule1","there is no rule 6")
          ) {
            r => {
              info(s"Remember Rule6 returned: $r")
              whenReady(sbcactor ? ShowMemory) {
                r => info(s"ShowMemory returned: $r")
              }
            }
          }
        whenReady(sbcactor ? Metrics) {
          r => {
            val registry = r.asInstanceOf[MetricRegistry]
            info(s"Metrics: ${registry.toString}")
            registry.getMetrics.keySet.size should be > (1)
            val reportBuffer = new ByteArrayOutputStream
            val reportStream = new PrintStream(reportBuffer)
            val infoReporter = ConsoleReporter.forRegistry(registry)
              .convertRatesTo(TimeUnit.SECONDS)
              .convertDurationsTo(TimeUnit.MILLISECONDS).outputTo(reportStream).build()
            infoReporter.start(1, TimeUnit.SECONDS)
            infoReporter.report()
            infoReporter.stop
            info(reportBuffer.toString)
          }
        }
      }

      runOn(node2) {
        system.actorOf(Props[SbcActor], "sbcactor")
        info("I made an sbcactor")
        enterBarrier("deployed")
      }

      enterBarrier("finished")
    }
  }

}