import cats.effect.testing.scalatest.AsyncIOSpec
import domain.{Feminine, Masculine, Neuter, Noun}
import infrastructure.Dictionary
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import service.{ArticleQuiz, Wordlookup}

class ArticleQuizSpec extends AsyncFlatSpec with AsyncIOSpec with Matchers {

  private val quiz = new ArticleQuiz()
  private val nouns = List(
    Noun(Masculine, "Tisch", "Tische", List("table")),
    Noun(Neuter, "Haus", "Häuser", List("house")),
    Noun(Feminine, "Katze", "Katzen", List("cat"))
  )
  private val dict = Dictionary.buildLookup(nouns)

  "the random noun function" should "return a defined noun" in {
    val noun = quiz.pickRandomNoun(dict)
    nouns should contain(noun)
  }





}
