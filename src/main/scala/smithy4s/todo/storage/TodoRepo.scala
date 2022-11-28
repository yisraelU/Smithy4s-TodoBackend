package smithy4s
package todo
package storage

import example.todo.{Id, Order, Title, Todo, TodoDescription}

trait TodoRepo[F[_]] {

  def createTodo(
      title: Title,
      order: Option[Order],
      description: Option[TodoDescription]
  ): F[Id]

  def getTodo(id: Id): F[Option[Todo]]

  def updateTodo(
      id: Id,
      name: Option[Title],
      description: Option[TodoDescription],
      order: Option[Order],
      completed: Option[Boolean]
  ): F[Todo]

  def deleteTodo(id: Id): F[Unit]
  def deleteAll(): F[Unit]

  def listTodos(): F[List[Todo]]

}
