package com.example.event.handler

import com.example.event.processor.EventProcessor
import org.scalatest.funsuite.AnyFunSuite

class EventProcessorSuite extends AnyFunSuite {

  test("Processor Should handle only events of the registered class") {
    val handler   = new EventHandlerImpl[UserEvent]
    val processor = EventProcessor[UserEvent].addHandler[UserCreated](handler)

    UserEvent.testEvents.foreach(processor.process)

    assert(handler.processedEvents == List(UserEvent.userCreated))
  }

  test("Processor Should handle events of all registered classes") {
    val handler = new EventHandlerImpl[UserEvent]
    val processor = EventProcessor[UserEvent]
      .addHandler[UserCreated](handler)
      .addHandler[NameChanged](handler)
      .addHandler[EmailChanged](handler)
      .addHandler[UserDeleted.type](handler)

    UserEvent.testEvents.foreach(processor.process)

    assert(handler.processedEvents == UserEvent.testEvents)
  }

  test("Multiple handlers can handle the same event") {
    val handler       = new EventHandlerImpl[UserEvent]
    val secondHandler = new EventHandlerImpl[UserEvent]
    val processor = EventProcessor[UserEvent]
      .addHandler[UserCreated](handler)
      .addHandler[UserCreated](secondHandler)

    UserEvent.testEvents.foreach(processor.process)

    assert(handler.processedEvents == List(UserEvent.userCreated))
    assert(secondHandler.processedEvents == List(UserEvent.userCreated))
  }

  test(
    "Registered handler for parent trait will not process event of child classes"
  ) {
    val handler = new EventHandlerImpl[UserEvent]
    val processor = EventProcessor[UserEvent]
      .addHandler[UserEvent](handler)
      .addHandler[UserChanged](handler)

    UserEvent.testEvents.foreach(processor.process)

    assert(handler.processedEvents.isEmpty)
  }
}
