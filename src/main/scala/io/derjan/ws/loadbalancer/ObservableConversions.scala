package io.derjan.ws.loadbalancer

import rx.Observable.OnSubscribe

import _root_.scala.util.{Failure, Success}
import scala.concurrent.{Future, Promise}
import rx.{Subscriber, Observable}
import rx.lang.scala

object ObservableConversions {
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
