import cats.effect.{IO, IOApp}
import domain.Noun
import infrastructure.{Dictionary, DictionaryLoader, FileLoader, JsonLoader, NounDictionaryLoader}
import com.typesafe.config.ConfigFactory
import service.{ArticleQuiz, ModeSelector, VocabMode, Wordlookup}

object Main extends IOApp.Simple {

  private val config = ConfigFactory.load()
  private val maxDistance = config.getInt("wortschatz.max-distance")

  private val fileLoader: FileLoader = new JsonLoader
  private val dictionaryLoader: DictionaryLoader[Noun] = new NounDictionaryLoader
  private val wordlookup: Wordlookup = new Wordlookup(maxDistance)
  private val vocabMode: VocabMode = new VocabMode(wordlookup)
  private val articleQuiz: ArticleQuiz = new ArticleQuiz
  private val modeSelector: ModeSelector = new ModeSelector(vocabMode, articleQuiz)

  def run: IO[Unit] = {
    for {
      json <- fileLoader.load("data/dictionary.json")
      loaded <- dictionaryLoader.load(json)
      dict = Dictionary.buildLookup(loaded)
      _ <- modeSelector.run(dict)
    } yield ()
  }

}
