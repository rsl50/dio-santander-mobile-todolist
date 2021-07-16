package br.com.dio.todolist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.dio.todolist.databinding.ActivityAddTaskBinding
import br.com.dio.todolist.extensions.format
import br.com.dio.todolist.extensions.text
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Using view binding.
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    }
}