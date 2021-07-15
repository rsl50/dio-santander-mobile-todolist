package br.com.dio.todolist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.dio.todolist.databinding.ActivityAddTaskBinding

class AddTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Using view binding
        val binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}