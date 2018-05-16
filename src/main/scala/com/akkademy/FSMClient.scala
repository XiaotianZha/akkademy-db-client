package com.akkademy

import akka.actor.{ActorIdentity, FSM, Identify}
import com.akkademy.StateContainerTypes.RequestQueue
import com.akkademy.message._

import scala.concurrent.duration._

sealed trait State
case object Disconnected extends State
case object Connected extends State


object StateContainerTypes {
  type RequestQueue = List[Request]
}

class FSMClient (remoteDBPath: String) extends FSM[State, RequestQueue]{


  import scala.concurrent.ExecutionContext.Implicits.global

  override def preStart:Unit={
    context.system.scheduler.schedule(0 seconds, 2 seconds, self,"heartBeat")
  }

  startWith(Disconnected, List.empty[Request])

  when(Disconnected){
    case Event("heartBeat", container:RequestQueue) =>
      context.actorSelection(remoteDBPath) ! Identify(remoteDBPath)
      stay using container
    case Event(ActorIdentity(_,None),container:RequestQueue) =>
      stay using container
    case Event(ActorIdentity(_,Some(remoteRef)),container:RequestQueue) =>
      //send message and change state
      if (container.headOption.nonEmpty){
        remoteRef ! container
      }
      goto(Connected) using Nil
  }

  when(Connected){
    case Event("heartBeat", container:RequestQueue) =>
      context.actorSelection(remoteDBPath) ! Identify(remoteDBPath)
      stay using container
    case Event(ActorIdentity(_,None),container:RequestQueue) =>
      //change state to Disconnected
      goto(Disconnected) using container
    case Event(x: Request, container: RequestQueue) =>
      stay using(container :+ x)
    case Event(ActorIdentity(_,Some(remoteRef)),container:RequestQueue) =>
      if (container.headOption.nonEmpty){
        remoteRef ! container
      }
      goto(Connected) using Nil
  }

}
