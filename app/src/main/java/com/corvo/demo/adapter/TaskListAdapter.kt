package com.corvo.demo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.corvo.demo.R
import com.corvo.demo.db.TaskDto
import com.corvo.demo.helper.TaskStatus
import kotlinx.android.synthetic.main.item_task.view.*


interface BrandsListener{
    fun onItemClicked(item: TaskDto)
    fun onFinished(item: TaskDto)
}
class TaskListAdapter(val context: Context, val items : List<TaskDto>, val listener: BrandsListener, val isVertical: Boolean? = false) : RecyclerView.Adapter<TaskListAdapter.Holder>() {

    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(inflater.inflate(R.layout.item_task,parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val item = items[position]
        holder.apply {
//

            holder.itemContainer.setOnClickListener {
                listener.onItemClicked(item)
            }

            if (item.status.equals(TaskStatus.FINISHED.status))holder.imgDone.setImageResource(R.drawable.ic_check)
            else holder.imgDone.setImageResource(R.drawable.ic_uncheck)

            holder.imgDone.setOnClickListener {
                listener.onFinished(item)
                holder.imgDone.setImageResource(R.drawable.ic_check)
            }

            taskName.text = item.taskName
            taskDescription.text = item.description
            timeAndDate.text = item.date


        }
    }

    inner class Holder(val view : View) : RecyclerView.ViewHolder(view){

        val imgDone = view.imgDone
        val itemContainer = view.itemContainer

        val taskName = view.taskName
        val taskDescription = view.tvDescription
        val timeAndDate = view.dateAndTime
    }
}