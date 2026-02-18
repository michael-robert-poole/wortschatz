package service

import cats.data.{NonEmptyList, NonEmptyMap}
import cats.effect.IO
import domain.Noun.formatNoun
import domain.{Article, Noun}

import scala.util.Random

class ArticleQuiz {
  private val quitCommands = Set(":quit", ":exit", ":back")

  def quizMode(dict: NonEmptyMap[String, Noun]): IO[Unit] = {
    for {
      _ <- IO.println("Starting quiz! Type the correct article (der/die/das). :back to return.")
      _ <- nextQuestion(dict)
    } yield ()
  }

  private def nextQuestion(dict: NonEmptyMap[String, Noun]): IO[Unit] = {
    for {
      noun <- IO(pickRandomNoun(dict))
      _ <- IO.println(s"\nWhat is the article for: ${noun.word}?")
      _ <- askQuestion(noun, dict)
    } yield ()
  }

  private def askQuestion(noun: Noun, dict: NonEmptyMap[String, Noun]): IO[Unit] = {
    for {
      guess <- IO.blocking(scala.io.StdIn.readLine())
      _ <- if (quitCommands.contains(guess.trim.toLowerCase)) IO.println("Returning to lookup mode.")
           else guess.toLowerCase match {
             case "der" | "die" | "das" =>
               val correct = Article.articleLookup(noun.gender).toString.toLowerCase
               (if (guess.toLowerCase == correct) {git
                 IO.println(s"Correct! ${formatNoun(noun)}")
               } else {
                 IO.println(s"Wrong! ${formatNoun(noun)}")
               }) *> nextQuestion(dict)
             case _ =>
               IO.println(s"Invalid input: $guess. Please enter der, die, or das.") *> askQuestion(noun, dict)
           }
    } yield ()
  }

  def pickRandomNoun(dict: NonEmptyMap[String, Noun]): Noun = {
    val keys: NonEmptyList[String] = dict.keys.toNonEmptyList
    val randomKey = Random.shuffle(keys.toList).head
    dict.lookup(randomKey).get


  }


}
