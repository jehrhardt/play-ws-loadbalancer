package io.derjan.ws.loadbalancer

import org.scalatest._
import rx.Observable

import scala.concurrent._
import scala.concurrent.duration._

class ObservableConversionsSpec extends FunSpec with Matchers {
  import ObservableConversions._

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
