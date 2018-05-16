package com.akkademy

import akka.actor.{Actor, ActorIdentity, ActorLogging, Identify}
import com.akkademy.msg.AskStateRequest

class IdentifyClient(remoteDBPath:String) extends Actor with ActorLogging{

  var state:Boolean =false

  override def preStart:Unit={
    context.actorSelection(remoteDBPath) ! Identify(remoteDBPath)
  }
  override def receive: Receive = {
    case ActorIdentity(p,None) =>
      log.info("remoteDB not available")
//      sender() ! "remoteDB not available"
      state =false
    case ActorIdentity(p,Some(remoteRef)) =>
      log.info("remoteDB  available")
//      sender() ! "remoteDB  available"
      state = true
    case AskStateRequest =>
      sender() ! s"remoteDB available flag ${state}"

  }
}
