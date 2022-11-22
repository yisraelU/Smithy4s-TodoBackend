package example.todo.Service

import cats.effect.IO
import cats.effect.std.UUIDGen
import example.todo.TodoId

trait TodoIdGen[F[_]] {
  def generateId: F[TodoId]
}

object TodoIdGen extends TodoIdGen[IO] {
  override def generateId: IO[TodoId] =
    for { id <- UUIDGen[IO].randomUUID.map(_.toString) } yield TodoId(id)
}
