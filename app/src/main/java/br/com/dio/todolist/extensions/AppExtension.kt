package br.com.dio.todolist.extensions

import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

private val locale = Locale("pt", "BR")

fun Date.format() : String {
    return SimpleDateFormat("dd/MM/yyy", locale).format(this)
}
// Creating an extension field 'text' to the TextInputLayout to allow setting/getting the text value
// of the field.
var TextInputLayout.text : String
    // Creating a get function to the field that returns "" in case of 'null'.
    get() = editText?.text?.toString() ?: ""
    set(value) {
        editText?.setText(value)
    }