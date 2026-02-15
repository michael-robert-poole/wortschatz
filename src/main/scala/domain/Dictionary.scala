package domain

import domain.models.Noun

object Dictionary {
  def buildLookup(nouns: List[Noun]): Map[String, Noun] =
    nouns.map(noun => noun.word.toLowerCase -> noun).toMap
}
