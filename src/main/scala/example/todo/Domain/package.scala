package example.todo

package object Domain {

  implicit class TodoOps(val todo: Todo) extends AnyVal {
    def update(
        name: Option[TodoName],
        description: Option[TodoDescription],
        status: Option[TodoStatus]
    ): Todo =
      todo.copy(
        name = name.getOrElse(todo.name),
        description = description.orElse(todo.description),
        status = status.getOrElse(todo.status)
      )
  }

}
