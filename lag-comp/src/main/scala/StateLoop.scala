package lagcomp

class StateLoop[Input, State](
      initialState: State,
      nextState: (State, List[Action[Input]]) => State) {
  
  var eventsSoFar: List[Event[Input]] = List()
  val cache = new WeakMap[List[Event[Input]], State](100)
  
  def stateAt(time: Int): State = {
    // If one clock is ahead of the other we might receive inputs from the future.
    val events = eventsSoFar.dropWhile(_.time > time)
    computeState(time, events)
  }
  
  def receive(event: Event[Input]): Unit = {
    val (newer, older) = eventsSoFar.span(_.time > event.time)
    eventsSoFar = newer ::: event :: older
  }
  
  def computeState(time: Int, events: List[Event[Input]]): State = {
    if(time == 0) {
      initialState
    } else {
      cache.getOrElseUpdate(time, events, {
        val (nowEvents, prevEvents) = events.span(_.time == time)
        nextState(computeState(time - 1, prevEvents), nowEvents.map(_.move))
      })
    }
  }
}
