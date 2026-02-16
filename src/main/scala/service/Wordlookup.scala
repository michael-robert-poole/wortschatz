package service

import domain.models.{Article, Noun}

class Wordlookup(maxDistance: Int) {

  private def formatNoun(noun: Noun): String =
    s"${Article.articleLookup(noun.gender)} ${noun.word} (plural: ${noun.plural}) translates to ${noun.meanings.mkString(", ")}"

  def wordlookup(word: String, dict: Map[String, Noun]): String = {
    dict.get(word.toLowerCase) match {
      case Some(value) => formatNoun(value)
      case None =>
        val candidates = dict.map { case (k, _) => (k, levenshteinDist(k, word)) }.filter(_._2 <= maxDistance)
        if (candidates.isEmpty) {
          "Noun is not on the learning curriculum"
        } else {
          val minDist = candidates.values.min
          val bestKey = candidates.filter(_._2 == minDist).keys.min
          val noun = dict(bestKey)
          s"Did you mean: ${noun.word}?\n${formatNoun(noun)}"
        }
    }
  }

  def getClosestWord(candidates: Map[String, Int]) = {
    val minDist = candidates.values.min //getClosestWordByDist
    val bestKey = candidates.filter(_._2 == minDist).keys.min //tiebreakByReturnAlphabetically first
  }

  def levenshteinDist(s1: String, s2: String): Int = {
    val matrix = Array.ofDim[Int](s1.length+1, s2.length+1)
    // Set first row
    for (j <- 0 to s2.length) matrix(0)(j) = j
    // Set first column
    for (i <- 0 to s1.length) matrix(i)(0) = i

    for (i <- 1 to s1.length) {
      for (j <- 1 to s2.length) {
        val cost = if (s1(i-1).toLower == s2(j-1).toLower) 0 else 1
        val sub = matrix(i-1)(j-1) + cost
        val del = matrix(i-1)(j) + 1
        val ins = matrix(i)(j-1) + 1
        matrix(i)(j) = Seq(sub, del, ins).min
      }
    }
    matrix.last.last
  }
}
