package example.todo.HttpApi

import cats.effect.{IO, Resource, ResourceIO}
import cats.implicits.toSemigroupKOps
import example.todo.TodoService
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.HttpMiddleware
import org.http4s.server.middleware.{CORS, RequestLogger, ResponseLogger}
import smithy4s.http4s.SimpleRestJsonBuilder

object Routes extends Http4sDsl[IO] {

  private val health: HttpRoutes[IO] = HttpRoutes.of[IO] { case GET -> Root =>
    Ok("hi there")
  }

  private val corsMiddleware: HttpRoutes[IO] => HttpRoutes[IO] =
    CORS.policy.withAllowOriginAll.httpRoutes _

  private val loggers: HttpRoutes[IO] => HttpRoutes[IO] = {
    { http: HttpRoutes[IO] =>
      RequestLogger.httpRoutes(true, false)(http)
    } andThen { http: HttpRoutes[IO] =>
      ResponseLogger.httpRoutes(true, false)(http)
    }
  }
  private val allMiddleware = corsMiddleware compose loggers

  def apply(todoService: TodoService[IO]): ResourceIO[HttpRoutes[IO]] = {
    val primary: Resource[IO, HttpRoutes[IO]] =
      SimpleRestJsonBuilder.routes(todoService).resource
    val docs: HttpRoutes[IO] = smithy4s.http4s.swagger.docs[IO](TodoService)
    primary.map(_ <+> docs <+> health).map(allMiddleware.apply)
  }
}
