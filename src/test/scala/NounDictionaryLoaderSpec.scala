import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

class NounDictionaryLoaderSpec extends AsyncFlatSpec with AsyncIOSpec with Matchers {

  private val testString =
    """
      |[
      |  { "gender": "das", "word": "Haus", "plural": "Häuser", "meanings": ["house"] },
      |  { "gender": "der", "word": "Tisch", "plural": "Tische", "meanings": ["table"] },
      |  { "gender": "die", "word": "Katze", "plural": "Katzen", "meanings": ["cat"] }
      |]
      |""".stripMargin

  private val invalidGenderTestString =
    """
      |[
      |  { "gender": "tu", "word": "Haus", "plural": "Häuser", "meanings": ["house"] },
      |  { "gender": "der", "word": "Tisch", "plural": "Tische", "meanings": ["table"] },
      |  { "gender": "die", "word": "Katze", "plural": "Katzen", "meanings": ["cat"] }
      |]
      |""".stripMargin

  private val dictionaryLoader = new NounDictionaryLoader
  "the noun dictionary loader" should "correctly return List of Nouns" in {
    dictionaryLoader.load(testString).map {
      result => result.size shouldBe 3
    }
  }

  "the noun dictionary loader" should "return an error for invalid gender" in {
    dictionaryLoader.load(invalidGenderTestString)
      .assertThrows[io.circe.DecodingFailure]
  }
}
