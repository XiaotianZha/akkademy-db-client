package com.akkademy

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import com.akkademy.msg.AskStateRequest
import akka.pattern.ask
import org.scalatest.{FunSpecLike, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

class IdentifyClientSpec extends FunSpecLike with Matchers{

  private implicit val timeout = Timeout(2 seconds)
  private implicit val system = ActorSystem("localSystem")

  val path ="akka.tcp://akkademy@127.0.0.1:2552/user/akkademy-db"

  private val client = system.actorOf(Props(new IdentifyClient(path)))

  describe("ask client state"){
    it("should return false"){
      val futureResult = client ? AskStateRequest
      val result = Await.result(futureResult,2 seconds)
      result should equal("remoteDB available flag false")
    }
    it("should return true"){
      //need wait for the ActorIdentity message
      Thread.sleep(6000)
      val futureResult = client ? AskStateRequest
      val result = Await.result(futureResult,10 seconds)
      result should equal("remoteDB available flag true")
    }
  }

}
