package com.akkademy

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.akkademy.msg.{AskStateRequest, IdentifySwapClient}
import org.scalatest.{FunSpecLike, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

class IdentifySwapClientSpec extends FunSpecLike with Matchers{

  private implicit val timeout = Timeout(2 seconds)
  private implicit val system = ActorSystem("localSystem")

  val path ="akka.tcp://akkademy@127.0.0.1:2552/user/akkademy-db"

  private val client = system.actorOf(Props(new IdentifySwapClient(path)))

  describe("ask client state"){
    it("should return false"){
      val futureResult = client ? AskStateRequest
      val result = Await.result(futureResult,2 seconds)
      result should equal("offline")
    }
    it("should return true"){
      //need wait for the ActorIdentity message
      Thread.sleep(6000)
      val futureResult = client ? AskStateRequest
      val result = Await.result(futureResult,10 seconds)
      result should equal("online")
    }
  }

}
