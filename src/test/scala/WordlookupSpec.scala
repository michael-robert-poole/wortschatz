import cats.effect.testing.scalatest.AsyncIOSpec
import domain.Dictionary
import domain.models.{Feminine, Masculine, Neuter, Noun}
import service.Wordlookup
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

class WordlookupSpec extends AsyncFlatSpec with AsyncIOSpec with Matchers {

  private val wordlookup = new Wordlookup()
  private val nouns = List(
    Noun(Masculine, "Tisch", "Tische", List("table")),
    Noun(Neuter, "Haus", "Häuser", List("house")),
    Noun(Feminine, "Katze", "Katzen", List("cat"))
  )
  private val dict = Dictionary.buildLookup(nouns)

  "the Wordlookup" should "return a translation when Noun exists" in {
    wordlookup.wordlookup("Haus", dict) should include("Haus")
  }

  it should "find a word regardless of input case" in {
    wordlookup.wordlookup("haus", dict) should include("Haus")
    wordlookup.wordlookup("HAUS", dict) should include("Haus")
    wordlookup.wordlookup("hAuS", dict) should include("Haus")
  }

  it should "return a not found message when Noun does not exist" in {
    wordlookup.wordlookup("Wrong", dict) shouldBe "Noun is not on the learning curriculum"
  }
}
