package com.akkademy

import akka.actor.{Actor, ActorIdentity, ActorSystem, Identify, Props}
import akka.event.Logging
import akka.stream.Supervision.Restart

import scala.concurrent.duration._

class IdentifyHeartbeatClient (remoteDBPath:String) extends Actor{

  val log = Logging(context.system, this)

  var tryTimes=0

  import scala.concurrent.ExecutionContext.Implicits.global

  override def preStart:Unit={
    context.system.scheduler.schedule(0 seconds, 2 seconds, self,"heartBeat")
  }
  override def receive: Receive = {
    case "heartBeat" =>
      log.info("send heartbeat")
      context.actorSelection(remoteDBPath) ! Identify(remoteDBPath)
    case ActorIdentity(_,None) =>
      log.info("lost heartBeat")
      tryTimes=tryTimes+1
      if (tryTimes>=2){
        log.info(s"lost heartbeat ${tryTimes}s, need restart")
        //Restart will not reset tryTimes
        tryTimes = 0
        Restart
      }
    case ActorIdentity(_,Some(_)) =>
      log.info("remoteDB  available")
      tryTimes=0
  }
}

object Main extends App{
  private implicit val system = ActorSystem("localSystem")

  val path ="akka.tcp://akkademy@127.0.0.1:2552/user/akkademy-db"

  private val client = system.actorOf(Props(new IdentifyHeartbeatClient(path)))
}
