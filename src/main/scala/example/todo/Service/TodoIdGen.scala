package example.todo.Service

import cats.effect.IO
import cats.effect.std.UUIDGen
import example.todo.Id

trait TodoIdGen[F[_]] {
  def generateId: F[Id]
}

object TodoIdGen extends TodoIdGen[IO] {
  override def generateId: IO[Id] =
    for { id <- UUIDGen[IO].randomUUID.map(_.toString) } yield Id(id)
}
