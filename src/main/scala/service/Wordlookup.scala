package service

import domain.models.{Article, Noun}

class Wordlookup {

  private def formatNoun(noun: Noun): String =
    s"${Article.articleLookup(noun.gender)} ${noun.word} (plural: ${noun.plural}) translates to ${noun.meanings.mkString(", ")}"

  def wordlookup(word: String, dict: Map[String, Noun]): String = {
    dict.get(word.toLowerCase) match {
      case Some(value) => formatNoun(value)
      case None =>
        val candidates = dict.map { case (k, _) => (k, levenshteinDist(k, word)) }.filter(_._2 <= 2)
        if (candidates.isEmpty) {
          "Noun is not on the learning curriculum"
        } else {
          val bestKey = candidates.minBy(_._2)._1
          val noun = dict(bestKey)
          s"Did you mean: ${noun.word}?\n${formatNoun(noun)}"
        }
    }
  }

  def levenshteinDist(s1: String, s2: String): Int = {
    val v1: Vector[Int] = (0 to s2.length).toVector

    val lastRow = s1.toLowerCase.zipWithIndex.foldLeft(v1) { case (prevRow, (c1, i)) =>
      s2.toLowerCase.zipWithIndex.foldLeft(Vector(i + 1)) { case (curRow, (c2, j)) =>
        val cost = if (c1 == c2) 0 else 1
        curRow :+ Seq(
          prevRow(j + 1) + 1,       // deletion
          curRow(j) + 1,             // insertion
          prevRow(j) + cost          // substitution
        ).min
      }
    }
    lastRow.last
  }
}
