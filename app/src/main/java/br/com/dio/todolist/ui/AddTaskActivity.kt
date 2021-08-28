package br.com.dio.todolist.ui

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.dio.todolist.dao.TaskDAO
import br.com.dio.todolist.database.AppDatabase
import br.com.dio.todolist.databinding.ActivityAddTaskBinding
import br.com.dio.todolist.extensions.format
import br.com.dio.todolist.extensions.text
import br.com.dio.todolist.model.Task
import br.com.dio.todolist.util.ResultCode
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var taskDao: TaskDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Using view binding.
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get database instance
        taskDao = AppDatabase.getInstance(this).getTaskDAO()

        if (intent.hasExtra(TASK_ID)) {
            // Capture task id from intent
            val taskId = intent.getIntExtra(TASK_ID, 0)
            // Bind task data from intent
            taskDao.get(taskId).let {
                binding.tilTitle.text = it.title
                binding.tilDescription.text = it.description
                binding.tilDate.text = it.date
                binding.tilHour.text = it.hour
            }
        }

        insertListeners()
    }

    private fun insertListeners() {
        binding.tilDate.editText?.setOnClickListener {
            // Selecting the date
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            // Getting the OK button click on the datePicker.
            datePicker.addOnPositiveButtonClickListener {
                // Using the extension function to format the date and fill the date field.
                //binding.tilDate.editText?.setText(Date(it).format())

                // Adjust the selected date using a timezone offset
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                // Using the extension field to set the formated date to the field.
                binding.tilDate.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.tilHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .build()

            timePicker.addOnPositiveButtonClickListener{
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                val minute = if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                binding.tilHour.text = "$hour:$minute"
            }

            timePicker.show(supportFragmentManager, null)
        }

        binding.btnCancelTask.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

        binding.btnNewTask.setOnClickListener {
            // Creating a Task
            val task = Task(
                    title = binding.tilTitle.text,
                    description = binding.tilDescription.text,
                    date = binding.tilDate.text,
                    hour = binding.tilHour.text,
                    id = intent.getIntExtra(TASK_ID, 0)
            )
            // Inserting the task in the database
            if (task.title != null && task.title.isNotEmpty()) {

                if (task.id == 0) {
                    taskDao.add(task)
                    setResult(ResultCode.ADD_OK.value)
                } else {
                    taskDao.edit(task)
                    setResult(ResultCode.EDIT_OK.value)
                }
            } else {
                setResult(ResultCode.CANCELED.value)
            }

            finish()
        }

        binding.toolbar.setNavigationOnClickListener {
            setResult(ResultCode.CANCELED.value)
            finish()
        }
    }

    companion object {
        const val TASK_ID = "task_id"
    }
}