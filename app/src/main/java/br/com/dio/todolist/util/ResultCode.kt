package br.com.dio.todolist.util

enum class ResultCode(val value: Int){
    ADD_OK(1),
    EDIT_OK(2),
    DELETE_OK(3),
    CANCELED(4),
    ERROR(-1)
}