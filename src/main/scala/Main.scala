import cats.effect.{IO, IOApp}
import domain.{Dictionary, DictionaryLoader}
import domain.models.Noun
import json.{FileLoader, JsonLoader}
import service.Wordlookup

object Main extends IOApp.Simple {

  private val fileLoader: FileLoader = new JsonLoader
  private val dictionaryLoader: DictionaryLoader[Noun] = new NounDictionaryLoader
  private val wordlookup: Wordlookup = new Wordlookup

  def run: IO[Unit] = {
    for {
      json <- fileLoader.load("data/dictionary.json")
      loaded <- dictionaryLoader.load(json)
      dict = Dictionary.buildLookup(loaded)
      l <- runLoop(dict)
    } yield ()
  }

  private val quitCommands = Set(":quit", ":exit", "-1")

  def runLoop(dict: Map[String, Noun]): IO[Unit] = {
    def innerLoop: IO[Unit] = for {
      _ <- IO.println("Enter a word to lookup (or :quit to exit):")
      word <- IO.blocking(scala.io.StdIn.readLine())
      _ <- if (quitCommands.contains(word.trim.toLowerCase)) IO.println("Auf Wiedersehen!")
           else IO.println(wordlookup.wordlookup(word, dict)) >> innerLoop
    } yield ()
    innerLoop
  }
}
