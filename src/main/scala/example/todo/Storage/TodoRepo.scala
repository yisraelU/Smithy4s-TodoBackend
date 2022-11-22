package example.todo.Storage

import example.todo._

trait TodoRepo[F[_]] {
  def createTodo(
      title: TodoName,
      description: Option[TodoDescription]
  ): F[TodoId]

  def getTodo(id: TodoId): F[Option[Todo]]

  def updateTodo(
      id: TodoId,
      name: Option[TodoName],
      description: Option[TodoDescription],
      status: Option[TodoStatus]
  ): F[Unit]

  def deleteTodo(id: TodoId): F[Unit]

  def listTodos(): F[List[Todo]]

}
