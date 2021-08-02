package br.com.dio.todolist.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.com.dio.todolist.databinding.ActivityAddTaskBinding
import br.com.dio.todolist.datasource.TaskDataSource
import br.com.dio.todolist.extensions.format
import br.com.dio.todolist.extensions.text
import br.com.dio.todolist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Using view binding.
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
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
            // Inserting the task in the data source list
            if (task.title != null && task.title.isNotEmpty()) {
                TaskDataSource.insertTask(task)
                Log.e("TODO", "insertListeners: " + TaskDataSource.getList())

                // Set the Activity result as OK to the caller Activity
                setResult(Activity.RESULT_OK)
            } else {
                setResult(Activity.RESULT_CANCELED)
            }

            finish()
        }
    }

    companion object {
        const val TASK_ID = "task_id"
    }
}