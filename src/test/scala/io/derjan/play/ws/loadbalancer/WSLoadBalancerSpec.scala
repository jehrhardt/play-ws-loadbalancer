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

import org.scalatest._

import scala.concurrent._
import scala.concurrent.duration._

import com.netflix.loadbalancer._

class WSLoadBalancerSpec extends FunSpec with Matchers {
  describe("A load balancer") {
    val servers = Seq(new Server("foo", 8080), new Server("foo", 8081))
    val loadBalancer = newLoadBalancer(servers)

    it("should execute the call with an endpoint") {
      val response = loadBalancer.withEndpoint(Future.successful)
      Await.result(response, 5.seconds) should be ("foo:8081")
    }

    it("should execute another call with another endpoint") {
      val response = loadBalancer.withEndpoint(Future.successful)
      Await.result(response, 5.seconds) should be ("foo:8080")
    }
  }

  def newLoadBalancer(servers: Seq[Server]): WSLoadBalancer = {
    import scala.collection.JavaConverters._

    val builder = LoadBalancerBuilder.newBuilder[Server]()
    val loadBalancer = builder.buildFixedServerListLoadBalancer(servers.asJava)
    new WSLoadBalancer(loadBalancer)
  }
}
