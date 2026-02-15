package json

import cats.effect.IO

trait FileLoader {
  def load(path: String): IO[String]
}
