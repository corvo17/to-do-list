package com.corvo.demo.fragment

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.corvo.demo.R
import com.corvo.demo.base.BaseFragment
import com.corvo.demo.base.BaseSimpleBottomSheetDialog
import com.corvo.demo.db.TaskDto
import com.corvo.demo.helper.Constants
import com.corvo.demo.helper.FrequencyList
import com.corvo.demo.helper.TaskStatus
import com.ibotta.android.support.pickerdialogs.SupportedDatePickerDialog
import kotlinx.android.synthetic.main.fragment_task_edit_or_add.*
import java.util.*


class TaskAddOrEditFragment : BaseFragment(), SupportedDatePickerDialog.OnDateSetListener {

    var oldTask: TaskDto? = null

    var listener: ITaskOperation? = null

    private var taskName = ""
    private var taskDescription = ""
    private var taskTime = ""
    private var taskDate = ""
    private var frequency = -1
    private var status = TaskStatus.ACTIVE.status

    override fun getLayout(): Int {
        return R.layout.fragment_task_edit_or_add
    }

    override fun setupViews() {
        getBaseActivity {
            back_btn.setOnClickListener { view ->
                it.onBackPressed()
            }

            calendarContainer.setOnClickListener { view ->
                SupportedDatePickerDialog(it,R.style.SpinnerDatePickerDialogTheme,this,2020,11,1).show()
            }

            timeContainer.setOnClickListener { view ->
                setTime()
            }

            frequencyContainer.setOnClickListener { view ->
                frequencyBtnClicked()
            }

            btn_done.setOnClickListener { view ->
                val currentTime = System.currentTimeMillis()
                taskName = etTaskName.text.toString()
                taskDescription = etDescription.text.toString()

                Log.i("Fuckk", (oldTask == null).toString())
                if (oldTask == null){
                    if (taskName.isBlank()){
                        it.showErrorMessage(getString(R.string.task_name_required))
                    }else{
                        it.onBackPressed()
                        val newTask = TaskDto( taskName, taskDescription, taskDate, taskTime, status, frequency, currentTime)
                        Log.i("Fuckk", newTask.toString())
                        listener?.onNewTaskAdded(newTask)
                    }
                }else {
                    oldTask!!.taskName = taskName
                    oldTask!!.description = taskDescription
                    oldTask!!.date = taskDate
                    oldTask!!.time = taskTime
                    oldTask!!.status = status
                    oldTask!!.updatedDate = currentTime

                    it.onBackPressed()
                    listener?.onTaskEdited(oldTask!!)
                }
            }

            btn_delete.setOnClickListener { view ->
                it.onBackPressed()
                if (oldTask != null)listener?.onTaskDeleted(oldTask!!)
            }

            setOldTaskFields()
        }
    }

    private fun setOldTaskFields() {
        getBaseActivity {
            if (oldTask != null){
                taskTime = oldTask!!.time
                taskDate = oldTask!!.date
                status = oldTask!!.status

                etTaskName.setText(oldTask!!.taskName)
                etDescription.setText(oldTask!!.description)
                tvTime.text = oldTask!!.time
                tvDate.text = oldTask!!.date

                if (oldTask!!.frequency != -1){
                    when(oldTask!!.frequency){
                        1 ->{
                            tvFrequency.text = FrequencyList.EVERY_HOUR.text
                        }
                        2 ->{
                            tvFrequency.text = FrequencyList.EVERY_TWO.text
                        }
                        3->{
                            tvFrequency.text = FrequencyList.EVERY_THREE.text
                        }
                        4->{
                            tvFrequency.text = FrequencyList.EVERY_FOUR.text
                        }
                        5->{
                            tvFrequency.text = FrequencyList.EVERY_FIVE.text
                        }
                    }
                }
            }
        }
    }

    private fun frequencyBtnClicked() {
        getBaseActivity {
            val list = listOf(
                FrequencyList.EVERY_HOUR.text,
                FrequencyList.EVERY_TWO.text,
                FrequencyList.EVERY_THREE.text,
                FrequencyList.EVERY_FOUR.text,
                FrequencyList.EVERY_FIVE.text
            )
            it.showBottomSheetDialog(getString(R.string.choose_frequency), list) { position ->
                tvFrequency.text = list.get(position)
                when (list.get(position)) {
                    FrequencyList.EVERY_HOUR.text -> {
                        frequency = FrequencyList.EVERY_HOUR.frequency
                    }
                    FrequencyList.EVERY_TWO.text -> {
                        frequency = FrequencyList.EVERY_TWO.frequency
                    }
                    FrequencyList.EVERY_THREE.text -> {
                        frequency = FrequencyList.EVERY_THREE.frequency
                    }
                    FrequencyList.EVERY_FOUR.text -> {
                        frequency = FrequencyList.EVERY_FOUR.frequency
                    }
                    FrequencyList.EVERY_FIVE.text -> {
                        frequency = FrequencyList.EVERY_FIVE.frequency
                    }
                }
            }
        }
    }

    override fun getTitle(): String {
        return "MainFrament"
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        var myMoth = ""
        var day = ""
        if (dayOfMonth < 10) day = "0$dayOfMonth"
        else day = "$dayOfMonth"

        if (month + 1 < 10) myMoth = "0${month + 1}"
        else myMoth = "${month + 1}"

        taskDate = "$day.$myMoth.$year"
        tvDate.text = taskDate
    }

    private fun setTime() {
        getBaseActivity {
            val mcurrentTime: Calendar = Calendar.getInstance()
            val hour: Int = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute: Int = mcurrentTime.get(Calendar.MINUTE)
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(
                it,
                { timePicker, selectedHour, selectedMinute ->
                    taskTime = "$selectedHour:$selectedMinute"
                    tvTime.text = taskTime
                },
                hour,
                minute,
                true
            ) //Yes 24 hour time
            mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }
    }

}

interface ITaskOperation{
    fun onNewTaskAdded(task: TaskDto)
    fun onTaskEdited(task: TaskDto)
    fun onTaskDeleted(task: TaskDto)
}