package smithy4s.todo.Domain

import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import example.todo._
import smithy4s.todo.Domain.TodoImpl.url
import smithy4s.todo.storage.TodoRepo

class TodoImpl(todoRepo: TodoRepo[IO]) extends TodoService[IO] {

  override def createTodo(
      title: Title,
      order: Option[Order],
      description: Option[TodoDescription]
  ): IO[TodoOutput] =
    todoRepo.createTodo(title,order, description).map { id =>
      TodoOutput(id, title, completed = false, url(id),order)
    }

  override def getTodo(id: Id): IO[TodoOutput] =
    todoRepo.getTodo(id).flatMap {
      case Some(todo) => IO.pure(TodoOutput(todo.id, todo.title, todo.completed, todo.url))
      case None       => IO.raiseError(TodoNotFound("Todo not found"))
    }

  override def updateTodo(
      id: Id,
      name: Option[Title],
      order: Option[Order],
      description: Option[TodoDescription],
      completed: Option[Boolean]
  ): IO[TodoOutput] =
    todoRepo.updateTodo(id, name, description, order  ,completed).map { todo =>
      TodoOutput(todo.id, todo.title, todo.completed, todo.url, todo.order )
    }

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
