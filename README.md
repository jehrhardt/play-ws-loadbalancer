# play-ws-loadbalancer
Client site load balancer for Play 2 WS module based on Netflix' Ribbon.

[![Build Status](https://travis-ci.org/jehrhardt/play-ws-loadbalancer.svg?branch=master)](https://travis-ci.org/jehrhardt/play-ws-loadbalancer)[![Download](https://api.bintray.com/packages/jehrhardt/maven/play-ws-loadbalancer/images/download.svg) ](https://bintray.com/jehrhardt/maven/play-ws-loadbalancer/_latestVersion)

## Requirements
This module requires the Play framework version 2.4.x and works with Scala 2.11 and 2.10.

## Usage
Add the WS and the load balancer module to your project's dependencies in `build.sbt`.

```scala
libraryDependencies ++= Seq(
  ws,
  "io.derjan" %% "play-ws-loadbalancer" % "0.1.0"
)
```

Create a load balancer object with a Ribbon load balancer, that fits your needs.

```scala
import scala.collection.JavaConverters._

val servers = Seq(new Server("localhost", 8080), new Server("localhost", 8081))
val builder = LoadBalancerBuilder.newBuilder[Server]()
val loadBalancer = new WSLoadBalancer(builder.buildFixedServerListLoadBalancer(servers.asJava))
```

The above example will use a Ribbon load balancer with a fixed list of servers. You can than inject the load balancer into your controller and wrap WS calls with it to get an endpoint from the server list.

```scala
class Application(ws: WSClient, loadBalancer: WSLoadBalancer) extends Controller {
  def index = Action.async {
    val response = loadBalancer withEndpoint { endpoint =>
      ws.url(s"$endpoint/my-path").get()
    }

    response map { result =>
      Ok(views.html.index(result))
    }
  }
}
```
