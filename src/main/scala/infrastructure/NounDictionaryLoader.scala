package infrastructure

import cats.effect.IO
import domain.Noun
import io.circe.jawn.decode
import infrastructure.NounDecoders._

class NounDictionaryLoader extends DictionaryLoader[Noun] {

  def load(json: String): IO[List[Noun]] = {
    IO.fromEither(decode[List[Noun]](json))
  }
}
