package com.example.event.handler

trait EventHandler[Event] {
  def handle(event: Event)
}


class EventHandlerImpl[Event] extends (Event => Unit) {
  private var events: List[Event] = List()

  def processedEvents: List[Event] = events

  override def apply(event: Event): Unit = events :+= event
}

