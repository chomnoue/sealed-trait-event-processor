package com.example.event.processor

import scala.reflect.macros.blackbox.Context

import language.experimental.macros

object EventProcessorMacro {
  def addHandlers[Event](processor: EventProcessor[Event], handler: Event => Unit): EventProcessor[Event]
  = macro setEventHandlers_impl[Event]


  def setEventHandlers_impl[Event: c.WeakTypeTag](c: Context)
                                                 (processor: c.Expr[EventProcessor[Event]], handler: c.Expr[Event=> Unit]):
  c.Expr[EventProcessor[Event]] = {

    import c.universe._

    val symbol = weakTypeOf[Event].typeSymbol

    if (!symbol.isClass) c.abort(
      c.enclosingPosition,
      "Can only register classes and subclasses"
    ) else if (!symbol.asClass.isSealed) c.abort(
      c.enclosingPosition,
      "Can only register subclasses of sealed classes"
    ) else {

      def subclasses(symbol: Symbol): List[Symbol] = {
        val children = symbol.asClass.knownDirectSubclasses.toList
        symbol :: children.flatMap(subclasses(_))
      }

      val children = subclasses(symbol)

      val calls = children.foldLeft(q"$processor")((current, ref) => q"$current.addHandler[$ref]($handler)")
      c.Expr[EventProcessor[Event]](calls)
    }
  }

}
