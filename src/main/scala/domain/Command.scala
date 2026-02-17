package domain

sealed trait Command
case object Quiz extends Command
case object Vocab extends Command

case class CommandError(err: String)

