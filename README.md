# scala-lag-comp

Tiny Scala/Scala.js framework to build collaborative applications such as Google Docs or a [multiplayer game](https://github.com/OlivierBlanvillain/survivor). In addition to the cool functional architecture, the framework does predictive latency compensation. Concretely, end users get the maximum responsiveness (same as if working locally) and the framework takes care ensuring *eventual consistency* of the application between the different peers.

It's a proof-of-concept but could probably serve as a basis to build something more serious. More information can be found on [this report](https://github.com/OlivierBlanvillain/master-thesis), chapter 3 is about the framework.


## Setup

It's on Sonatype Snapshots, compiled for Scala 2.11 and Scala.js 0.6.0+:

```scala
resolvers +=
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies +=
  "com.github.olivierblanvillain" %%% "scala-lag-comp" % "0.1-SNAPSHOT"
```

## Getting Started

It's peer-to-peer, and only supports two peers at the moment:

```scala
sealed trait Peer
case object P1 extends Peer
case object P2 extends Peer
```

Peers perform some `Actions`s that are exchanged on the network:

```scala
case class Action[Input](input: Input, peer: Peer)
```

The behavior of the application is entirely defined by a `nextState` function that takes as argument the `State` of the application in the previous frame, a list of `Action`s emitted during that frame, and returns the `State` for the next frame. Everything must be immutable and `nextState` must be a pure function.

```scala
nextState: (State, List[Action[Input]]) => State
```

The rendering is by the side effects of a function that takes as argument the local peer and the current `State`:

```scala
render: (Peer, State) => Unit
```

Putting it all together, the interface of the framework looks like this:

```scala
class Engine[Input, State](
    initState: State,
    nextState: (State, List[Action[Input]]) => State,
    render: (Peer, State) => Unit
    connection: ConnectionHandle) {
  
  def triggerRendering(): Unit
  def futureAct: Future[Input => Unit]
}
```

Here the `connection` should be a link between the two peers, `triggerRendering` should be called when the platform is ready to display the next frame and `futureAct` is used to emit local user `Input`s.

That's all there is to know to use the framework, checkout [this game](https://github.com/OlivierBlanvillain/survivor) for a cool example.
