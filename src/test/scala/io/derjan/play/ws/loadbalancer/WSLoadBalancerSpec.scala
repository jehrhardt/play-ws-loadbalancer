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
