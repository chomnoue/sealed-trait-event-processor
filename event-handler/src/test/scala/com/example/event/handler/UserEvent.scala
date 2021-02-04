package com.example.event.handler

sealed trait UserEvent {

}

final case class UserCreated(name:String, email:String) extends UserEvent
sealed trait UserChanged extends UserEvent
final case class NameChanged(name:String) extends UserChanged
final case class EmailChanged(email:String) extends UserChanged
case object UserDeleted extends UserEvent

case object UserEvent{
  val userCreated: UserCreated = UserCreated("Test name", "test@email.com")
  val nameChanged: NameChanged = NameChanged("New name")
  val emailChanged: EmailChanged = EmailChanged("new@email.com")

  val testEvents: List[UserEvent] = List(userCreated, nameChanged, emailChanged, UserDeleted)
}
