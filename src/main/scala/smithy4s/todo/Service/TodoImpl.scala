package smithy4s.todo.Service

import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import example.todo._
import smithy4s.todo.Service.TodoImpl.url
import smithy4s.todo.storage.TodoRepo

class TodoImpl(todoRepo: TodoRepo[IO]) extends TodoService[IO] {

  override def createTodo(
      title: Title,
      order: Option[Order],
      description: Option[TodoDescription]
  ): IO[Todo] =
    todoRepo.createTodo(title, order, description).map { id =>
      Todo(id, title, completed = false, url(id), description, order)
    }

  override def getTodo(id: Id): IO[Todo] =
    todoRepo.getTodo(id).flatMap {
      case Some(todo) =>
        IO.pure(
          Todo(
            todo.id,
            todo.title,
            todo.completed,
            todo.url,
            todo.description,
            todo.order
          )
        )
      case None => IO.raiseError(TodoNotFound("Todo not found"))
    }

  override def updateTodo(
      id: Id,
      name: Option[Title],
      order: Option[Order],
      description: Option[TodoDescription],
      completed: Option[Boolean]
  ): IO[Todo] =
    todoRepo.updateTodo(id, name, description, order, completed)

  override def deleteTodo(id: Id): IO[Unit] =
    todoRepo.deleteTodo(id)

  override def listTodos(): IO[ListTodosOutput] =
    todoRepo.listTodos().map(ListTodosOutput(_))

  override def apiVersion(): IO[ApiVersionOutput] =
    IO.pure(sys.env.getOrElse("HEROKU_SLUG_COMMIT", "1.0.0"))
      .map(ApiVersionOutput(_))

  override def deleteAll(): IO[Unit] = todoRepo.deleteAll()
}

object TodoImpl {

  val host = "https://todo-smithy4s.herokuapp.com/todo"
  def url(id: Id): Url = Url(s"$host/$id")
  def apply(todoRepo: TodoRepo[IO]): IO[TodoService[IO]] = {
    new TodoImpl(todoRepo).pure[IO]
  }
}
