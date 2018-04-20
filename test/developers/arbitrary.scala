package developers

import developers.model.Developer
import org.scalacheck.Gen

object arbitrary {

  val arbitraryDeveloper: Gen[Developer] = for {
    id <- Gen.uuid
    username <- Gen.alphaNumStr
    email <- Gen.option(Gen.alphaLowerStr)
  } yield Developer(id, username, email)

}
