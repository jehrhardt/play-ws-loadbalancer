/* Copyright 2015 Jan Ehrhardt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
