package service

import cats.effect.IO

class ArticleQuiz {
  def quizMode(): IO[String] = {
    IO.pure("Quiz")
  }
}
