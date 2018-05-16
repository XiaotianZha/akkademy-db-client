package com.akkademy.msg

import akka.actor.{Actor, ActorIdentity, Identify, Stash}
import akka.event.Logging

class IdentifySwapClient(remoteDBPath:String) extends Actor with Stash{

  val log = Logging(context.system, this)

  override def preStart:Unit={
    context.actorSelection(remoteDBPath) ! Identify(remoteDBPath)
  }

  override def receive: Receive = {
    case ActorIdentity(p,None) =>
      log.info("remoteDB not available")
    case AskStateRequest =>
      sender() ! "offline"
    case ActorIdentity(p,Some(remoteRef)) =>
      log.info("remoteDB  available")
      context.become(online)
  }

  def online:Receive ={
    case AskStateRequest =>
      sender() ! "online"
  }
}
