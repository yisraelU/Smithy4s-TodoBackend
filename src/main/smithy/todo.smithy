$version: "2.0"

namespace example.todo

use smithy4s.api#simpleRestJson

@simpleRestJson
service TodoService {
    version: "1.0"
    operations: [CreateTodo, GetTodo, UpdateTodo, DeleteTodo, ListTodos]
}

@http(method: "POST", uri: "/todo")
operation CreateTodo {
    input: CreateTodoInput
    output: CreateTodoOutput

}

@http(method: "GET", uri: "/todo/{id}")
@readonly
operation GetTodo {
    input: GetTodoInput
    output: GetTodoOutput
    errors: [TodoNotFound]
}

@http(method: "PUT", uri: "/todo/{id}")
@idempotent
operation  UpdateTodo {
    input: UpdateTodoInput
    errors: [TodoNotFound]
}

@idempotent
@http(method: "DELETE", uri: "/todo/{id}")
operation  DeleteTodo {
    input: DeleteTodoInput
    errors: [TodoNotFound]
}

@readonly
@http(method: "GET", uri: "/todo")
operation  ListTodos {
    output: ListTodosOutput

}

string Id

string Title

string TodoDescription

structure Todo {
    @required
    id: Id
    @required
    title: Title
    description: TodoDescription
    @required
    status: TodoStatus
    order: Integer
}


enum TodoStatus {
    OPEN
    CLOSED
}
structure CreateTodoInput {
    @required
    title: Title
    description: TodoDescription
}

structure CreateTodoOutput {
    @required
    Id: Id
}

structure GetTodoInput {
    @required
    @httpLabel
    id: Id
}

structure GetTodoOutput {
    @required
    todo: Todo
}

@error("client")
@httpError(404)
structure TodoNotFound {
    @required
    message: String
}

structure UpdateTodoInput {
    @required
    @httpLabel
    id: Id
    title: Title
    description: TodoDescription
    status: TodoStatus
}

structure DeleteTodoInput {
    @httpLabel
    @required
    id: Id
}

list TodoList {
    member: Todo
}

structure ListTodosOutput {
    @required
    todos: TodoList
}