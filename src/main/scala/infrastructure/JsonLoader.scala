package infrastructure

import cats.effect.{IO, Resource}

import scala.io.Source

class JsonLoader extends FileLoader {

  def load(path: String): IO[String] = {
    Resource.fromAutoCloseable(IO(Source.fromFile(path)))
      .use(source => IO(source.mkString))
  }
}
