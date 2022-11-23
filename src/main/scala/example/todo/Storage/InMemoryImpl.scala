package example.todo.Storage

import cats.effect.{IO, Ref}
import example.todo._
import example.todo.Domain.TodoOps
import example.todo.Service.TodoIdGen

class InMemoryImpl(ref: Ref[IO, Map[Id, Todo]], todoIdGen: TodoIdGen[IO])
    extends TodoRepo[IO] {
  override def createTodo(
      title: Title,
      description: Option[TodoDescription]
  ): IO[Id] =
    for {
      id <- todoIdGen.generateId
      _ <- ref.update(todos =>
        todos + (id -> Todo(id, title, completed = false, description))
      )
    } yield id

  override def getTodo(id: Id): IO[Option[Todo]] =
    ref.get.map(_.get(id))

  override def updateTodo(
      id: Id,
      name: Option[Title],
      description: Option[TodoDescription],
      completed: Option[Boolean]
  ): IO[Unit] =
    ref.update { todos =>
      todos.get(id) match {
        case Some(value) =>
          todos + (id -> value.update(name, description, completed))
        case None => todos
      }
    }

  override def deleteTodo(id: Id): IO[Unit] = {
    ref.update(todos => todos - id)
  }

  override def listTodos(): IO[List[Todo]] = {
    ref.get.map(_.values.toList)
  }

  override def deleteAll(): IO[Unit] =
    ref.update(_ => Map.empty)
}

object InMemoryImpl {
  def apply(todoIdGen: TodoIdGen[IO]): IO[InMemoryImpl] = {
    Ref
      .of[IO, Map[Id, Todo]](Map.empty)
      .map(ref => new InMemoryImpl(ref, todoIdGen))
  }
}
