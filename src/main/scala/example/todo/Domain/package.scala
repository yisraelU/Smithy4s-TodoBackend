package example.todo

package object Domain {

  implicit class TodoOps(val todo: Todo) extends AnyVal {
    def update(
        title: Option[Title],
        description: Option[TodoDescription],
        status: Option[TodoStatus]
    ): Todo =
      todo.copy(
        title = title.getOrElse(todo.title),
        description = description.orElse(todo.description),
        status = status.getOrElse(todo.status)
      )
  }

}
