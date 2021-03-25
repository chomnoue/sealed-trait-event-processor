package com.example.event.handler

import com.example.event.processor.{EventProcessor, EventProcessorMacro}
import org.scalatest.funsuite.AnyFunSuite

class EventProcessorMacroSuite extends AnyFunSuite {

  test(
    "Calling macro for parent trait will register handler for child classes"
  ) {
    val handler = new EventHandlerImpl[UserEvent]
    val processor =
      EventProcessorMacro.addHandlers(EventProcessor[UserEvent], handler)

    UserEvent.testEvents.foreach(processor.process)

    assert(handler.processedEvents == UserEvent.testEvents)
  }
}
