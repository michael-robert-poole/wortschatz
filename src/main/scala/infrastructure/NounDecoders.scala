package infrastructure

import domain.{Feminine, Gender, Masculine, Neuter, Noun}
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
