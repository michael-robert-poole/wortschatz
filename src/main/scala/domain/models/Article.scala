package domain.models

sealed trait Article
case object Der extends Article
case object Die extends Article
case object Das extends Article
object Article {
   def  articleLookup(gender: Gender): Article = {
     gender match {
       case Masculine => Der
       case Feminine => Die
       case Neuter => Das
     }
   }
}
