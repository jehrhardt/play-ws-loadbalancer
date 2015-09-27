package io.derjan.play.ws.loadbalancer

import scala.concurrent.Future

import com.netflix.loadbalancer._
import com.netflix.loadbalancer.reactive._

import rx.Observable

class WSLoadBalancer(loadBalancer: ILoadBalancer) {
  def withEndpoint[T](call: (String) => Future[T]): Future[T] = {
    import observable.Conversions._

    val command = LoadBalancerCommand.builder[T]().withLoadBalancer(loadBalancer).build()
    command.submit(delegatingOperation[T](call))
  }

  private def delegatingOperation[T](wsCall: (String) => Future[T]): ServerOperation[T] = new ServerOperation[T] {
    override def call(server: Server): Observable[T] = {
      import observable.Conversions._

      val endpoint = server.getHostPort
      wsCall(endpoint)
    }
  }
}
