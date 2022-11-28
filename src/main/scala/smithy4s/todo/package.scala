package smithy4s

import example.todo.{Order, Title, Todo, TodoDescription}

package object todo {
  implicit class TodoOps(val todo: Todo) extends AnyVal {
    def update(
                title: Option[Title],
                description: Option[TodoDescription],
                order: Option[Order],
                completed: Option[Boolean]
              ): Todo =
      todo.copy(
        title = title.getOrElse(todo.title),
        description = description.orElse(todo.description),
        order = order.orElse(todo.order),
        completed = completed.getOrElse(false)
      )
  }
}
