package developers

import developers.storage.DevelopersDAO
import utils.{DatabaseCleanerOnEachTest, InMemoryDatabaseFlatSpec}
import arbitrary._
import org.scalatest.Matchers
import org.scalatest.prop.PropertyChecks
import utils.extensions._
import org.scalacheck.Gen

class DevelopersSpec
    extends InMemoryDatabaseFlatSpec
    with DatabaseCleanerOnEachTest
    with PropertyChecks
    with Matchers {

  private val developersDao = app.injector.instanceOf[DevelopersDAO]

  "Developers" should "be created and retrieved by id" in {
    forAll(arbitraryDeveloper) { developer =>
      val createdDeveloper = developersDao.createOrUpdate(developer).awaitForResult

      val obtainedDeveloper = developersDao.getById(developer.id).awaitForResult

      developer shouldBe createdDeveloper
      obtainedDeveloper shouldBe Some(developer)
    }
  }

  it should "return none if there are no developers persisted associated with the id passed as parameter" in {
    forAll(Gen.uuid) { id =>
      val developer = developersDao.getById(id).awaitForResult

      developer shouldBe None
    }
  }

}
