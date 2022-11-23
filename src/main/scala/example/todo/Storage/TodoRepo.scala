package example.todo.Storage

import example.todo._

trait TodoRepo[F[_]] {
  def createTodo(
      title: Title,
      description: Option[TodoDescription]
  ): F[Id]

  def getTodo(id: Id): F[Option[Todo]]

  def updateTodo(
      id: Id,
      name: Option[Title],
      description: Option[TodoDescription],
      status: Option[TodoStatus]
  ): F[Unit]

  def deleteTodo(id: Id): F[Unit]

  def listTodos(): F[List[Todo]]

}
