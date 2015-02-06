val commonSettings = Seq(
  organization := "com.github.olivierblanvillain",
  version := "0.1-SNAPSHOT",
  scalaVersion := "2.11.5",
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-unchecked",
    "-Yno-adapted-args",
    "-Ywarn-numeric-widen",
    "-Xfuture",
    "-Xlint"
  )
)

lazy val lagComp = crossProject
  .crossType(CrossType.Pure)
  .in(file("lag-comp"))
  .settings(commonSettings: _*)
  .settings(name := "scala-lag-comp")
  .settings(libraryDependencies ++= Seq(
    "com.github.olivierblanvillain" %%% "transport-core" % "0.1-SNAPSHOT",
    "com.lihaoyi" %%% "upickle" % "0.2.6",
    "org.monifu" %%% "minitest" % "0.11" % "test"))
  .jvmSettings(testFrameworks += new TestFramework("minitest.runner.Framework"))
lazy val lagCompJVM = lagComp.jvm
lazy val lagCompJS = lagComp.js

lazy val server = project
  .in(file("server"))
  .settings(commonSettings: _*)
  .settings(name := "scala-lag-comp-server")
  .settings(libraryDependencies +=
    "com.github.olivierblanvillain" %% "transport-netty" % "0.1-SNAPSHOT")
