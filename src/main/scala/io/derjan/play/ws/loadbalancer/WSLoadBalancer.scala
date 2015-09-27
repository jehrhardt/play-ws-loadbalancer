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
