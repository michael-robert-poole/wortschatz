package domain.models

sealed trait Gender
case object Masculine extends Gender
case object Feminine extends Gender
case object Neuter extends Gender

case class Noun(gender: Gender, word: String, plural: String, meanings: List[String])
