package com.akkademy

import org.scalatest.{FunSpecLike, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

class SClientIntegrationSpec extends FunSpecLike with Matchers{

  val client = new SClient("127.0.0.1:2552")

  describe("akkademyDbClient"){
    it("should set a value"){
      client.set("123",new Integer(123))
      val futureResult = client.get("123")
      val result = Await.result(futureResult, 10 seconds)
      result should equal(123)
    }
    it("should get KeyNotFoundException"){
      val futureResult = client.get("12")
      intercept[Exception]{
        Await.result(futureResult.mapTo[String], 1 seconds)
      }
    }
    it("should set value abc"){
      client.setIfNotExits("abc","abc")
      val futureResult = client.get("abc")
      val result = Await.result(futureResult, 10 seconds)
      result should equal("abc")
    }
    it("should get a 123"){
      client.setIfNotExits("123",new Integer(126))
      val futureResult = client.get("123")
      val result = Await.result(futureResult, 10 seconds)
      result should equal(123)
    }
  }

}
