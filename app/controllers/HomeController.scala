package controllers

import java.util.UUID
import javax.inject._

import developers.model.Developer
import developers.storage.DevelopersDAO
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class HomeController @Inject()(developersDAO: DevelopersDAO, cc: ControllerComponents)(implicit val ec: ExecutionContext) extends AbstractController(cc) {

  def index() = Action.async { implicit request: Request[AnyContent] =>
    developersDAO.createOrUpdate(Developer(UUID.randomUUID(), "pedro", Some("pedro@karumi.com"))).map { _ =>
      Ok(views.html.index())
    }
  }
}
