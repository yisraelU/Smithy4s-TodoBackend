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

string TodoId

string TodoName

string TodoDescription

structure Todo {
    @required
    id: TodoId
    @required
    name: TodoName
    description: TodoDescription
    @required
    status: TodoStatus
}


enum TodoStatus {
    OPEN
    CLOSED
}
structure CreateTodoInput {
    @required
    title: TodoName
    description: TodoDescription
}

structure CreateTodoOutput {
    @required
    TodoId: TodoId
}

structure GetTodoInput {
    @required
    @httpLabel
    id: TodoId
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
    id: TodoId
    name: TodoName
    description: TodoDescription
    status: TodoStatus
}

structure DeleteTodoInput {
    @httpLabel
    @required
    id: TodoId
}

list TodoList {
    member: Todo
}

structure ListTodosOutput {
    @required
    todos: TodoList
}