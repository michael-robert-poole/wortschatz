package infrastructure

import cats.data.NonEmptyMap
import cats.implicits._
import domain.Noun

import scala.collection.immutable.SortedMap

object Dictionary {
  def buildLookup(nouns: List[Noun]): NonEmptyMap[String, Noun] =
    NonEmptyMap.fromMapUnsafe(SortedMap(nouns.map(noun => noun.word.toLowerCase -> noun): _*))
}
