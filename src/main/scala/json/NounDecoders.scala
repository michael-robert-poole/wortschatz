package json

import domain.models._
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

object NounDecoders {
  implicit val genderDecoder: Decoder[Gender] = Decoder.decodeString.emap {
    case "der" => Right(Masculine)
    case "die" => Right(Feminine)
    case "das" => Right(Neuter)
    case other => Left(s"Unknown gender: $other")
  }

  implicit val nounDecoder: Decoder[Noun] = deriveDecoder[Noun]
}
