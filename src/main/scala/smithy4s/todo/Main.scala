package smithy4s
package todo

import cats.effect.{IO, IOApp, Resource}
import com.comcast.ip4s.{IpLiteralSyntax, Port}
import org.http4s.HttpRoutes
import org.http4s.ember.server.EmberServerBuilder
import smithy4s.todo.http.Routes
import smithy4s.todo.Service.{TodoIdGen, TodoImpl}
import smithy4s.todo.storage.InMemoryImpl

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
      port <- Resource.pure(
        sys.env.get("PORT").flatMap(Port.fromString).getOrElse(port"8080")
      )
      server <- EmberServerBuilder
        .default[IO]
        .withPort(port)
        .withHost(host"0.0.0.0")
        .withHttpApp(routes.orNotFound)
        .build

    } yield server
  }
}
