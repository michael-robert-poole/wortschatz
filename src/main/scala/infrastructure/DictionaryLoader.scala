package infrastructure

import cats.effect.IO

trait DictionaryLoader[A] {
  def load(source: String): IO[List[A]]
}
