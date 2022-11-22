package example.todo.Storage

import cats.effect.{IO, Ref}
import example.todo._
import example.todo.Domain.TodoOps
import example.todo.Service.TodoIdGen

class InMemoryImpl(ref: Ref[IO, Map[TodoId, Todo]], todoIdGen: TodoIdGen[IO])
    extends TodoRepo[IO] {
  override def createTodo(
      title: TodoName,
      description: Option[TodoDescription]
  ): IO[TodoId] =
    for {
      id <- todoIdGen.generateId
      _ <- ref.update(todos =>
        todos + (id -> Todo(id, title, TodoStatus.OPEN, description))
      )
    } yield id

  override def getTodo(id: TodoId): IO[Option[Todo]] =
    ref.get.map(_.get(id))

  override def updateTodo(
      id: TodoId,
      name: Option[TodoName],
      description: Option[TodoDescription],
      status: Option[TodoStatus]
  ): IO[Unit] =
    ref.update { todos =>
      todos.get(id) match {
        case Some(value) =>
          todos + (id -> value.update(name, description, status))
        case None => todos
      }
    }

  override def deleteTodo(id: TodoId): IO[Unit] = {
    ref.update(todos => todos - id)
  }

  override def listTodos(): IO[List[Todo]] = {
    ref.get.map(_.values.toList)
  }
}

object InMemoryImpl {
  def apply(todoIdGen: TodoIdGen[IO]): IO[InMemoryImpl] = {
    Ref
      .of[IO, Map[TodoId, Todo]](Map.empty)
      .map(ref => new InMemoryImpl(ref, todoIdGen))
  }
}
