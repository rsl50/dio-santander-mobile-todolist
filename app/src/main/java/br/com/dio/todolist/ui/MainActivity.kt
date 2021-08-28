package br.com.dio.todolist.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.dio.todolist.R
import br.com.dio.todolist.dao.TaskDAO
import br.com.dio.todolist.database.AppDatabase
import br.com.dio.todolist.databinding.ActivityMainBinding
import br.com.dio.todolist.util.ResultCode

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TaskListAdapter
    private lateinit var taskDao: TaskDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get database instance
        taskDao = AppDatabase.getInstance(this).getTaskDAO()

        adapter = TaskListAdapter(taskDao.all().toMutableList())
        binding.rvTasks.adapter = adapter
        updateList()

        insertListeners()
    }

    override fun onResume() {
        super.onResume()
        adapter.replaceAllTasks(taskDao.all())
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
            taskDao.remove(it)
            Toast.makeText(this, "Tarefa Removida", Toast.LENGTH_SHORT).show()
            updateList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Update recyclerView with list of tasks
        if (requestCode == CREATE_NEW_TASK) {
            when (resultCode) {
                ResultCode.ADD_OK.value -> {
                    Log.e(LOG_TAG, "Tarefa Criada")
                    Toast.makeText(this, "Tarefa Criada", Toast.LENGTH_SHORT).show()
                    updateList()
                }
                ResultCode.EDIT_OK.value -> {
                    Log.e(LOG_TAG, "Tarefa Atualizada")
                    Toast.makeText(this, "Tarefa Atualizada", Toast.LENGTH_SHORT).show()
                    updateList()
                }
                ResultCode.CANCELED.value -> {
                    Log.e(LOG_TAG, "Tarefa não Criada")
                    Toast.makeText(this, getString(R.string.task_not_created), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateList() {
        val list = taskDao.all()//TaskDataSource.getList()
        binding.includeEmpty.emptyState.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        adapter.replaceAllTasks(list)
    }

    companion object {
        private const val CREATE_NEW_TASK = 1000
        private const val LOG_TAG = "TODO"
    }
}