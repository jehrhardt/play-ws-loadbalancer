package io.derjan.play.ws.loadbalancer.observable

import org.scalatest._
import rx.Observable

import scala.concurrent._
import scala.concurrent.duration._

class ConversionsSpec extends FunSpec with Matchers {
  import Conversions._

  describe("Observable") {
    it("should be converted to a future") {
      val future: Future[String] = Observable.just("foo")
      Await.result(future, 5.seconds) should be ("foo")
    }
  }

  describe("Future") {
    it("should be converted to an observable") {
      val observable: Observable[String] = Future.successful("foo")
      observable.toBlocking.toFuture.get should be ("foo")
    }
  }
}
