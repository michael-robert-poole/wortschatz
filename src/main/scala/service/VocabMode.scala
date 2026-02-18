package service

import cats.data.NonEmptyMap
import cats.effect.IO
import domain.Noun

class VocabMode(wordlookup: Wordlookup) {

  private val quitCommands = Set(":quit", ":exit", "-1")
  def vocabBuilder(dict: NonEmptyMap[String, Noun]): IO[Unit] = {
    def innerLoop: IO[Unit] = for {
      _ <- IO.println("Enter a word to lookup (or :quit to exit):")
      word <- IO.blocking(scala.io.StdIn.readLine())
      _ <- if (quitCommands.contains(word.trim.toLowerCase)) IO.println("Auf Wiedersehen!")
      else IO.println(wordlookup.wordlookup(word, dict)) >> innerLoop
    } yield ()

    innerLoop
  }
}
