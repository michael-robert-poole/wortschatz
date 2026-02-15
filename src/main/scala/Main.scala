import cats.effect.{IO, IOApp}
import domain.DictionaryLoader
import domain.models.Noun
import json.{FileLoader, JsonLoader}

object Main extends IOApp.Simple {

  private val fileLoader: FileLoader = new JsonLoader
  private val dictionaryLoader: DictionaryLoader[Noun] = new NounDictionaryLoader

  def run: IO[Unit] = {
    for {
      json <- fileLoader.load("data/dictionary.json")
      loaded <- dictionaryLoader.load(json)
      _ <- IO.println(s"Successfully loaded $loaded")
    } yield ()
  }
}
