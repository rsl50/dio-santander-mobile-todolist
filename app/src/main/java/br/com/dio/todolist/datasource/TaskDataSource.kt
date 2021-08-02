package br.com.dio.todolist.datasource

import br.com.dio.todolist.model.Task

object TaskDataSource {
    private val list = arrayListOf<Task>()

    fun getList() = list.toList()

    fun insertTask(task: Task) {
        if (task.id == 0) {
            // Create a copy of the task object and set the id before adding to the list
            list.add(task.copy(id = list.size + 1))
        } else {
            list.remove(task)
            list.add(task)
        }
    }

    // Returns a task by its id
    fun findById(taskId: Int) = list.find { it.id == taskId }

    fun deleteTask(task: Task) {
        list.remove(task)
    }
}