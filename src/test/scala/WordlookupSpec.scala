import cats.effect.testing.scalatest.AsyncIOSpec
import domain.Dictionary
import domain.models.{Feminine, Masculine, Neuter, Noun}
import service.Wordlookup
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

class WordlookupSpec extends AsyncFlatSpec with AsyncIOSpec with Matchers {

  private val wordlookup = new Wordlookup(2)
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

  "levenshteinDist" should "return 2 for deletion of prefix and suffix" in {
    wordlookup.levenshteinDist("horse", "ors") shouldBe 2
  }

  it should "return 5 for multiple substitutions" in {
    wordlookup.levenshteinDist("Execution", "Intention") shouldBe 5
  }

  it should "return 0 for case-insensitive match" in {
    wordlookup.levenshteinDist("Kitten", "kitten") shouldBe 0
  }

  it should "return 3 for mixed insertions and substitutions" in {
    wordlookup.levenshteinDist("Kitten", "sitting") shouldBe 3
  }

  it should "return 1 for single character substitution" in {
    wordlookup.levenshteinDist("a", "b") shouldBe 1
  }

  it should "return length of non-empty string when other is empty" in {
    wordlookup.levenshteinDist("", "abc") shouldBe 3
    wordlookup.levenshteinDist("abc", "") shouldBe 3
  }

  it should "return 0 for two empty strings" in {
    wordlookup.levenshteinDist("", "") shouldBe 0
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
