package br.com.dio.todolist.datasource

import br.com.dio.todolist.model.Task

object TaskDataSource {
    private val list = arrayListOf<Task>()

    fun getList() = list

    fun insertTask(task: Task) {
        // Create a copy of the task object and set the id before adding to the list
        list.add(task.copy(id = list.size + 1))
    }
}