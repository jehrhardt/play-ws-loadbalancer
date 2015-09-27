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

import rx.Observable.OnSubscribe

import _root_.scala.util.{Failure, Success}
import scala.concurrent.{Future, Promise}
import rx.{Subscriber, Observable}
import rx.lang.scala

object Conversions {
  implicit def toFuture[T](observable: Observable[T]): Future[T] = {
    import scala.JavaConversions._

    val promise = Promise[T]()
    val scalaObservable: scala.Observable[T] = observable
    scalaObservable.subscribe(x => promise.success(x), e => promise.failure(e))
    promise.future
  }

  implicit def toObservable[T](future: Future[T]): Observable[T] = Observable.create(subscribeToFuture(future))

  private def subscribeToFuture[T](futureResult: Future[T]): OnSubscribe[T] = new OnSubscribe[T] {
    import play.api.libs.concurrent.Execution.Implicits.defaultContext

    def call(observer: Subscriber[_ >: T]): Unit = {
      futureResult onComplete {
        case Success(response) => if (!observer.isUnsubscribed) {
          observer.onNext(response)
          observer.onCompleted()
        }
        case Failure(error) => observer.onError(error)
      }
    }
  }
}
