package com.matrisync.sbcactor

import akka.contrib.pattern.ReceivePipeline
import akka.contrib.pattern.ReceivePipeline.HandledCompletely
import SbcActorProtocol.SbcActorRequest.Metrics
import nl.grons.metrics.scala._

trait InstrumentedActor extends DefaultInstrumented
 {
    this: ReceivePipeline =>
    pipelineOuter {
      case Metrics => {
       sender ! metricRegistry
       HandledCompletely
      }
     }
}




