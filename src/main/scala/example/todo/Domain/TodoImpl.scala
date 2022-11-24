package example.todo.Domain

import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import example.todo._
import example.todo.Domain.TodoImpl.url
import example.todo.Storage.TodoRepo

class TodoImpl(todoRepo: TodoRepo[IO]) extends TodoService[IO] {
  override def createTodo(
      title: Title,
      description: Option[TodoDescription]
  ): IO[CreateTodoOutput] =
    todoRepo.createTodo(title, description).map { id =>
      CreateTodoOutput(id, title, completed = false, url(id))
    }

  override def getTodo(id: Id): IO[GetTodoOutput] =
    todoRepo.getTodo(id).flatMap {
      case Some(todo) => IO.pure(GetTodoOutput(todo))
      case None       => IO.raiseError(TodoNotFound("Todo not found"))
    }

  override def updateTodo(
      id: Id,
      name: Option[Title],
      description: Option[TodoDescription],
      completed: Option[Boolean]
  ): IO[Unit] =
    todoRepo.updateTodo(id, name, description, completed)

  override def deleteTodo(id: Id): IO[Unit] =
    todoRepo.deleteTodo(id)

  override def listTodos(): IO[ListTodosOutput] =
    todoRepo.listTodos().map(ListTodosOutput(_))

  override def apiVersion(): IO[ApiVersionOutput] =
    IO.pure(sys.env.getOrElse("API_VERSION", "1.0.0")).map(ApiVersionOutput(_))

  override def deleteAll(): IO[Unit] = todoRepo.deleteAll()
}

object TodoImpl {

  val host = "https://todo-smithy4s.herokuapp.com/todo"
  def url(id: Id): Url = Url(s"$host/$id")
  def apply(todoRepo: TodoRepo[IO]): IO[TodoService[IO]] = {
    new TodoImpl(todoRepo).pure[IO]
  }
}
