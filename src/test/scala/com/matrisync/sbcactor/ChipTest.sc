val localConfig =
  """
 akka {
   stdout-loglevel = "OFF"
   loglevel = "OFF"
     actor {
     provider = remote
   }
   remote {
     enabled-transports = ["akka.remote.netty.tcp"]
     netty.tcp {
       hostname = "127.0.0.1"
       port = 2552
     }
   }
 }
   com.matrisync.SbcActor {
     configName = "SbcActorSpec config"
     foo = bar
   }

    """
import com.typesafe.config.ConfigFactory
val config = ConfigFactory.parseString(localConfig).resolve
//  val config = ConfigFactory.parseString(localConfig).withFallback(ConfigFactory.defaultApplication()).resolve
//  val config = ConfigFactory.defaultApplication.resolve
// val loader = ActorSystem.getClass.getClassLoader
import akka.actor.ActorSystem
import com.matrisync.sbcactor.SbcActorProtocol.SbcActorRequest._
import akka.pattern.ask
import scala.concurrent.duration._
import akka.util.Timeout
implicit val timeout = Timeout(30 seconds)
import scala.concurrent.Await

// implicit val system = ActorSystem("demo",config,loader)
implicit val system = ActorSystem("demo")
val selection = system.actorSelection("akka.tcp://Main@192.168.1.20:2552/user/app")
Await.result(selection.ask(DoEval("List(\"apples\", \"peaches\", \"pumpkin\")")),timeout.duration)
Await.result(selection.ask(DoEval("2 + 3")),timeout.duration)
Await.result(selection.ask(Remember("foo","bar")),timeout.duration)
Await.result(selection.ask(Remember("baz","radish")),timeout.duration)
Await.result(selection.ask(ShowMemory),timeout.duration)
