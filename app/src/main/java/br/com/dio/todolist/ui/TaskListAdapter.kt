package br.com.dio.todolist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import br.com.dio.todolist.R
import br.com.dio.todolist.databinding.ItemTaskBinding
import br.com.dio.todolist.model.Task

class TaskListAdapter(private val tasks: MutableList<Task> = mutableListOf()) :
    RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    // Empty functions for the popup menu lambdas
    var listenerEdit : (Task) -> Unit = {}
    var listenerDelete : (Task) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // Creating the layout inflater.
        val inflater = LayoutInflater.from(parent.context)
        // Binding the view using the parent as a guide, however we do not attach it to the parent.
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }

    fun add(tasks: List<Task>) {
        this.tasks.addAll(tasks)
        notifyItemRangeInserted(0, tasks.size)
    }

    fun replaceAllTasks(tasks: List<Task>) {
        this.tasks.clear()
        this.tasks.addAll(tasks)
        notifyDataSetChanged()
    }

    // In order to be able to access the lambdas we need to set it as inner class
    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Task) {
            binding.tvTitle.text = item.title
            binding.tvDate.text = "${item.date} ${item.hour}"
            binding.ivMore.setOnClickListener {
                showPopup(item)
            }
        }

        private fun showPopup(item: Task) {
            val ivMore = binding.ivMore
            val popupMenu = PopupMenu(ivMore.context, ivMore)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_edit -> listenerEdit(item)
                    R.id.action_delete -> listenerDelete(item)
                }
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }
    }
}