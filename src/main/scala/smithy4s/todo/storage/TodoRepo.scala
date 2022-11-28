package smithy4s
package todo
package storage

import example.todo.{Id, Title, Todo, TodoDescription}

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
      completed: Option[Boolean]
  ): F[Unit]

  def deleteTodo(id: Id): F[Unit]
  def deleteAll(): F[Unit]

  def listTodos(): F[List[Todo]]

}
