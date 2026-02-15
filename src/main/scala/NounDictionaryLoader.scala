import cats.effect.IO
import domain.DictionaryLoader
import domain.models.Noun
import io.circe.jawn.decode
import json.NounDecoders._

class NounDictionaryLoader extends DictionaryLoader[Noun] {

  def load(json: String): IO[List[Noun]] = {
    IO.fromEither(decode[List[Noun]](json))
  }
}
