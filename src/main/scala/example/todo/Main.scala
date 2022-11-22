package example.todo

import cats.effect.{IO, IOApp, Resource}
import com.comcast.ip4s.{IpLiteralSyntax, Port}
import example.todo.Domain.TodoImpl
import example.todo.HttpApi.Routes
import example.todo.Service.TodoIdGen
import example.todo.Storage.InMemoryImpl
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.HttpRoutes

object Main extends IOApp.Simple {
  override def run: IO[Unit] = {
    val program = for {
      memory <- Resource.eval(InMemoryImpl(TodoIdGen))
      service <- Resource.eval(TodoImpl(memory))
      routes <- Routes(service)
      res <- server(routes)
    } yield res

    program.use(_ => IO.never)

  }

  private def server(routes: HttpRoutes[IO]) = {
    for {
      port <- Resource.pure(sys.env.get("PORT").flatMap(Port.fromString).getOrElse(port"8080"))
      server <- EmberServerBuilder
        .default[IO]
        .withPort(port)
        .withHost(host"0.0.0.0")
        .withHttpApp(routes.orNotFound)
        .build

    } yield server
  }
}
