package utils

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

object extensions {

  implicit class RichFuture[T](future: Future[T]) {
    def awaitForResult: T = Await.result(future, Duration.Inf)
  }

}
