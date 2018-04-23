package developers

import developers.model.Developer
import org.scalacheck.Gen

object arbitrary {

  val arbitraryDeveloper: Gen[Developer] = for {
    id       <- Gen.uuid
    username <- Gen.alphaNumStr.suchThat(_.length < 255)
    email <- Gen.option(
      Gen.oneOf(
        Seq("pedro@karumi.com",
            "fran@karumi.com",
            "davide@karumi.com",
            "jorge@karumi.com",
            "sergio@karumi.com")))
  } yield Developer(id, username, email)

}
