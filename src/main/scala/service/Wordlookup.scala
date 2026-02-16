package service

import domain.models.{Article, Noun}

import scala.{+:, :+}
import scala.collection.:+

class Wordlookup {

  def wordlookup(word: String, dict: Map[String, Noun]): String = {
    dict.get(word.toLowerCase) match {
      case Some(value) => s"${Article.articleLookup(value.gender)} ${value.word} (plural: ${value.plural}) translates to ${value.meanings.mkString(", ")}"
      case None =>
        val closeToWord = dict.keys.filter(w => levenshteinDist(w, word) <= 2)
        if (closeToWord.isEmpty) {
          "Noun is not on the learning curriculum"
        } else {
          "Did you mean one of" + closeToWord.mkString(",")
        }
    }
  }

  def levenshteinDist(s1: String, s2: String) = {
    val v1:Vector[Int] = (0 to s2.length).toVector

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
