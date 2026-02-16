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

  "Horse vs ors" should "return 2" in {
    val s1 = "horse"
    val s2 = "ors"
    wordlookup.levenshteinDist(s1, s2) shouldBe 2
  }

  "Execution vs Intention" should "return 5" in {
    val s1 = "Execution"
    val s2 = "Intention"
    wordlookup.levenshteinDist(s1, s2) shouldBe 5
  }

  "Kitten vs kitten" should "return 0" in {
    val s1 = "Kitten"
    val s2 = "kitten"
    wordlookup.levenshteinDist(s1, s2) shouldBe 0
  }

  "fuzzy matching" should "suggest the closest word when input is close" in {
    val result = wordlookup.wordlookup("huus", dict)
    result should include("Did you mean: Haus?")
    result should include("Das Haus (plural: Häuser) translates to house")
  }

  it should "suggest Tisch for tsch" in {
    val result = wordlookup.wordlookup("tsch", dict)
    result should include("Did you mean: Tisch?")
    result should include("Der Tisch (plural: Tische) translates to table")
  }

  it should "return not found when no word is close" in {
    wordlookup.wordlookup("xyzzy", dict) shouldBe "Noun is not on the learning curriculum"
  }
}
