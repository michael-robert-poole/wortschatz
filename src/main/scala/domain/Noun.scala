package domain

sealed trait Gender
case object Masculine extends Gender
case object Feminine extends Gender
case object Neuter extends Gender

case class Noun(gender: Gender, word: String, plural: String, meanings: List[String])

object Noun {

  def formatNoun(noun: Noun): String =
    s"${Article.articleLookup(noun.gender)} ${noun.word} (plural: ${noun.plural}) translates to ${noun.meanings.mkString(", ")}"

}