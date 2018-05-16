package com.akkademy

import akka.actor.{ActorSelection, ActorSystem}
import akka.util.Timeout
import akka.pattern.ask
import com.akkademy.message.{DeleteRequest, GetRequest, SetIfNotExistsRequest, SetRequest}

import scala.concurrent.duration._

class SClient(remoteAddress:String) {
  private implicit val timeout = Timeout(2 seconds)
  private implicit val system = ActorSystem("localSystem")
  private val remoteDB:ActorSelection = system.actorSelection(s"akka.tcp://akkademy@$remoteAddress/user/akkademy-db")

  def set(key:String, value : Object) ={
    remoteDB ? SetRequest(key,value)
  }

  def get(key:String) ={
    remoteDB ? GetRequest(key)
  }

  def setIfNotExits(key:String, value:Object) ={
    remoteDB ? SetIfNotExistsRequest(key,value)
  }

  def delete(key:String) ={
    remoteDB ? DeleteRequest(key)
  }

}
