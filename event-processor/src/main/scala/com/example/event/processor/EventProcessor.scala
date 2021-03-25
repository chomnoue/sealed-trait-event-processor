package com.example.event.processor

import scala.reflect.ClassTag

trait EventProcessor[Event] {
  def addHandler[E <: Event: ClassTag](
      handler: E => Unit
  ): EventProcessor[Event]

  def process(event: Event)
}

object EventProcessor {

  type Handler[Event] = (_ <: Event) => Unit

  private case class EventProcessorImpl[Event](
      handlers: Map[Class[_ <: Event], List[Handler[Event]]] =
        Map[Class[_ <: Event], List[Handler[Event]]]()
  ) extends EventProcessor[Event] {

    override def addHandler[E <: Event: ClassTag](
        handler: E => Unit
    ): EventProcessor[Event] = {
      val eventClass =
        implicitly[ClassTag[E]].runtimeClass.asInstanceOf[Class[_ <: Event]]
      val eventHandlers = handler
        .asInstanceOf[Handler[Event]] :: handlers.getOrElse(eventClass, List())
      copy(handlers + (eventClass -> eventHandlers))
    }

    override def process(event: Event): Unit = {
      handlers
        .get(event.getClass)
        .foreach(_.foreach(_.asInstanceOf[Event => Unit].apply(event)))
    }
  }

  def apply[Event](): EventProcessor[Event] = EventProcessorImpl()
}
