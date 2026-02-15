package service

import domain.models.{Article, Noun}

class Wordlookup {

  def wordlookup(word: String, dict: Map[String, Noun]): String = {
    dict.get(word.toLowerCase) match {
      case Some(value) => s"${Article.articleLookup(value.gender)} ${value.word} (plural: ${value.plural}) translates to ${value.meanings.mkString(", ")}"
      case None => "Noun is not on the learning curriculum"
    }
  }
}
