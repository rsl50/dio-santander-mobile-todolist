package br.com.dio.todolist.ui

import android.app.Activity
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import br.com.dio.todolist.R
import br.com.dio.todolist.databinding.ActivityMainBinding
import br.com.dio.todolist.datasource.TaskDataSource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    // Lazy indicates the initialization will only happen when the adapter is called
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.adapter = adapter
        updateList()

        insertListeners()
    }

    private fun insertListeners() {
        binding.fab.setOnClickListener {
            // Adding listener on FAB to start 'AddTaskActivity'
            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)
        }

        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)
        }

        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            updateList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Update recyclerView with list of tasks
        if (requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK) {
            Log.e("TODO", "Task Criada/Atualizada")
            updateList()
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("TODO", "Task não Criada/Atualizada")
            Toast.makeText(this, getString(R.string.task_not_created), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateList() {
        val list = TaskDataSource.getList()
        binding.includeEmpty.emptyState.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        adapter.submitList(list)
    }

    companion object {
        private const val CREATE_NEW_TASK = 1000
    }
}