organization := "io.derjan"

name := "play-ws-loadbalancer"

version := "0.1.0"

scalaVersion := "2.11.7"

crossScalaVersions := Seq("2.10.5", "2.11.7")

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % "2.4.3" % "provided",
  "com.netflix.ribbon" % "ribbon-loadbalancer" % "2.1.1" exclude("commons-logging", "commons-logging"),
  "io.reactivex" %% "rxscala" % "0.25.0",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "ch.qos.logback" % "logback-classic" % "1.1.3" % "test",
  "org.slf4j" % "jcl-over-slf4j" % "1.7.12" % "test"
)

bintrayRepository := "maven"

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

pomExtra := (
  <url>https://github.com/jehrhardt/play-ws-loadbalancer</url>
  <scm>
    <url>git@github.com:jehrhardt/play-ws-loadbalancer.git</url>
    <connection>scm:git:git@github.com:jehrhardt/play-ws-loadbalancer.git</connection>
  </scm>
  <developers>
    <developer>
      <id>jehrhardt</id>
      <name>Jan Ehrhardt</name>
    </developer>
  </developers>
)
