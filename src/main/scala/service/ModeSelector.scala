package service

import cats.effect.IO
import domain.{Command, CommandError, Quiz, Vocab}
import domain.models.Noun

class ModeSelector(vocabMode: VocabMode, articleQuiz: ArticleQuiz) {

  private val quitCommands = Set(":quit", ":exit")

  def run(dict: Map[String, Noun]): IO[Unit] = {
    for {
      _ <- IO.println("Select a mode from:\n" +
        ":vocab\n" +
        ":quiz\n" +
        ":quit")
      command <- IO.blocking(scala.io.StdIn.readLine())
      _ <- if (quitCommands.contains(command.trim.toLowerCase)) IO.println("Auf Wiedersehen!")
           else mapStringToCommand(command) match {
             case Right(Quiz) => articleQuiz.quizMode().flatMap(IO.println) *> run(dict)
             case Right(Vocab) => vocabMode.vocabBuilder(dict) *> run(dict)
             case Left(err) => IO.println(err.err) *> run(dict)
           }
    } yield ()
  }

  private def mapStringToCommand(string: String): Either[CommandError, Command] = {
    string.toLowerCase match {
      case ":vocab" => Right(Vocab)
      case ":quiz" => Right(Quiz)
      case _ => Left(CommandError(s"Unknown command: $string"))
    }
  }
}
