scala> Await.result(selection.ask(DoEval("2 + 3")),timeout.duration)
res8: Any = 5

scala> Await.result(selection.ask(Remember("foo","bar")),timeout.duration)
res9: Any = Done

scala> Await.result(selection.ask(Remember("baz","radish")),timeout.duration)
res10: Any = Done

scala> Await.result(selection.ask(ShowMemory),timeout.duration)
res11: Any = Map(foo -> bar, baz -> radish)

scala>

scala> Await.result(selection.ask(DoEval("List(\"apples\", \"peaches\", \"pumpkin\")")),timeout.duration)
res12: Any = List(apples, peaches, pumpkin)

scala>


[INFO] [01/22/2017 13:47:29.979] [main] [akka.remote.Remoting] Starting remoting
[INFO] [01/22/2017 13:47:31.835] [main] [akka.remote.Remoting] Remoting started; listening on addresses :[akka.tcp://Main@192.168.1.20:2552]
[INFO] [01/22/2017 13:47:31.870] [main] [akka.remote.Remoting] Remoting now listens on addresses: [akka.tcp://Main@192.168.1.20:2552]
[INFO] [01/22/2017 13:47:32.624] [Main-akka.actor.default-dispatcher-3] [akka.tcp://Main@192.168.1.20:2552/user/app] sbcactorActor started: akka://Main/user/app
[DEBUG] [01/22/2017 14:11:35.577] [Main-akka.remote.default-remote-dispatcher-6] [akka.remote.Remoting] Associated [akka.tcp://Main@192.168.1.20:2552] <- [akka.tcp://demo@192.168.99.1:2552]
[DEBUG] [01/22/2017 14:11:36.228] [Main-akka.actor.default-dispatcher-2] [akka://Main/user/app] received handled message DoEval(List("apples", "peaches", "pumpkin")) from Actor[akka.tcp://demo@192.168.99.1:2552/temp/$a]
[DEBUG] [01/22/2017 14:12:07.839] [Main-akka.actor.default-dispatcher-2] [akka://Main/user/app] received handled message DoEval(2 + 3) from Actor[akka.tcp://demo@192.168.99.1:2552/temp/$b]
[WARN] [SECURITY][01/22/2017 14:12:07.878] [Main-akka.remote.default-remote-dispatcher-6] [akka.serialization.Serialization(akka://Main)] Using the default Java serializer for class [scala.collection.immutable.$colon$colon] which is not recommended because of performance implications. Use another serializer or disable this warning using the setting 'akka.actor.warn-about-java-serializer-usage'
[DEBUG] [01/22/2017 14:12:07.907] [Main-akka.remote.default-remote-dispatcher-6] [akka.serialization.Serialization(akka://Main)] Using serializer[akka.serialization.JavaSerializer] for message [scala.collection.immutable.$colon$colon]
[DEBUG] [01/22/2017 14:12:15.683] [Main-akka.remote.default-remote-dispatcher-6] [akka.serialization.Serialization(akka://Main)] Using serializer[akka.serialization.JavaSerializer] for message [java.lang.Integer]
[DEBUG] [01/22/2017 14:12:16.009] [Main-akka.actor.default-dispatcher-2] [akka://Main/user/app] received handled message Remember(foo,bar) from Actor[akka.tcp://demo@192.168.99.1:2552/temp/$c]
[WARN] [SECURITY][01/22/2017 14:12:16.019] [Main-akka.remote.default-remote-dispatcher-6] [akka.serialization.Serialization(akka://Main)] Using the default Java serializer for class [com.matrisync.sbcactor.SbcActorProtocol$SbcActorResponse$Done$] which is not recommended because of performance implications. Use another serializer or disable this warning using the setting 'akka.actor.warn-about-java-serializer-usage'
[DEBUG] [01/22/2017 14:12:16.020] [Main-akka.remote.default-remote-dispatcher-6] [akka.serialization.Serialization(akka://Main)] Using serializer[akka.serialization.JavaSerializer] for message [com.matrisync.sbcactor.SbcActorProtocol$SbcActorResponse$Done$]
[DEBUG] [01/22/2017 14:12:16.202] [Main-akka.actor.default-dispatcher-2] [akka://Main/user/app] received handled message Remember(baz,radish) from Actor[akka.tcp://demo@192.168.99.1:2552/temp/$d]
[DEBUG] [01/22/2017 14:12:32.947] [Main-akka.actor.default-dispatcher-14] [akka://Main/user/app] received handled message ShowMemory from Actor[akka.tcp://demo@192.168.99.1:2552/temp/$e]
[WARN] [SECURITY][01/22/2017 14:12:32.953] [Main-akka.remote.default-remote-dispatcher-6] [akka.serialization.Serialization(akka://Main)] Using the default Java serializer for class [scala.collection.immutable.Map$Map2] which is not recommended because of performance implications. Use another serializer or disable this warning using the setting 'akka.actor.warn-about-java-serializer-usage'
[DEBUG] [01/22/2017 14:12:32.957] [Main-akka.remote.default-remote-dispatcher-6] [akka.serialization.Serialization(akka://Main)] Using serializer[akka.serialization.JavaSerializer] for message [scala.collection.immutable.Map$Map2]
[DEBUG] [01/22/2017 14:13:02.639] [Main-akka.actor.default-dispatcher-14] [akka://Main/user/app] received handled message DoEval(List("apples", "peaches", "pumpkin")) from Actor[akka.tcp://demo@192.168.99.1:2552/temp/$f]
[DEBUG] [01/22/2017 14:42:56.389] [Main-akka.actor.default-dispatcher-16] [akka://Main/user/app] received handled message DoEval(List("apples", "peaches", "pumpkin")) from Actor[akka.tcp://demo@192.168.99.1:2552/temp/$g]
[DEBUG] [01/22/2017 14:44:11.948] [Main-akka.actor.default-dispatcher-17] [akka://Main/user/app] received handled message DoEval(List("apples", "peaches", "pumpkin")) from Actor[akka.tcp://demo@192.168.99.1:2552/temp/$h]
[DEBUG] [01/22/2017 14:44:21.032] [Main-akka.actor.default-dispatcher-17] [akka://Main/user/app] received handled message DoEval(2 + 3) from Actor[akka.tcp://demo@192.168.99.1:2552/temp/$i]
[DEBUG] [01/22/2017 14:44:26.412] [Main-akka.actor.default-dispatcher-17] [akka://Main/user/app] received handled message Remember(foo,bar) from Actor[akka.tcp://demo@192.168.99.1:2552/temp/$j]
[DEBUG] [01/22/2017 14:44:26.586] [Main-akka.actor.default-dispatcher-17] [akka://Main/user/app] received handled message Remember(baz,radish) from Actor[akka.tcp://demo@192.168.99.1:2552/temp/$k]
[DEBUG] [01/22/2017 14:44:26.692] [Main-akka.actor.default-dispatcher-17] [akka://Main/user/app] received handled message ShowMemory from Actor[akka.tcp://demo@192.168.99.1:2552/temp/$l]
[DEBUG] [01/22/2017 14:44:43.391] [Main-akka.actor.default-dispatcher-18] [akka://Main/user/app] received handled message DoEval(List("apples", "peaches", "pumpkin")) from Actor[akka.tcp://demo@192.168.99.1:2552/temp/$m]
