package com.corvo.demo.viewModel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.corvo.demo.db.LocalDB
import com.corvo.demo.db.TaskDto
import com.corvo.demo.helper.Constants
import com.corvo.demo.reciever.MyReciever
import com.corvo.demo.helper.PrefsUtil
import com.corvo.demo.helper.TaskStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TaskViewModel(private val localDB: LocalDB, private val pref: PrefsUtil, val context: Context) :

    ViewModel() {

    val isLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    val taskAdded = MutableLiveData<String>()
    val taskUpdated = MutableLiveData<String>()
    val taskDeleted = MutableLiveData<String>()

    val missedTasks = MutableLiveData<List<TaskDto>>()
    val activeTasks = MutableLiveData<List<TaskDto>>()
    val finishedTasks = MutableLiveData<List<TaskDto>>()

    init {
        Log.i("Created", "ViewModel")
    }

   fun updateTask(task: TaskDto){

                CoroutineScope(Dispatchers.Main).launch {
                    isLoading.value = true
                    try {


                    withContext(CoroutineScope(context = Dispatchers.Default).coroutineContext) {
                        localDB.getTaskDao().updateTask(task)
                    }
                        setNotifications(task)
                        taskUpdated.value = "Task successfully added"



                    isLoading.value = false

                }catch (exception: Exception){
                    isLoading.value = false
                    errorMessage.value = exception.message
                }
                }

    }

   fun addTask(task: TaskDto){

                CoroutineScope(Dispatchers.Main).launch {
                    isLoading.value = true
                    try {


                    withContext(CoroutineScope(context = Dispatchers.Default).coroutineContext) {
                        localDB.getTaskDao().addTask(task)
                    }
                        setNotifications(task)
                        taskAdded.value = "Task successfully added"


                    isLoading.value = false

                }catch (exception: Exception){
                    isLoading.value = false
                    errorMessage.value = exception.message
                }
                }

    }

   fun deleteTask(task: TaskDto){

                CoroutineScope(Dispatchers.Main).launch {
                    isLoading.value = true
                    try {


                    withContext(CoroutineScope(context = Dispatchers.Default).coroutineContext) {
                        localDB.getTaskDao().removeTask(task)
                    }
                        taskDeleted.value = "Task successfully deleted"



                    isLoading.value = false

                }catch (exception: Exception){
                    isLoading.value = false
                    errorMessage.value = exception.message
                }
                }

    }

   fun loadTaskList(){

                CoroutineScope(Dispatchers.Main).launch {
                    isLoading.value = true
                    try {
                        val _missedTasks: MutableList<TaskDto> = ArrayList()
                        val _activeTasks: MutableList<TaskDto> = ArrayList()
                        val _finishedTasks: MutableList<TaskDto> = ArrayList()
                        val _allTasks: List<TaskDto>

                    withContext(CoroutineScope(context = Dispatchers.Default).coroutineContext) {
                        _allTasks = localDB.getTaskDao().loadTasks()
                        Log.i("loadedTasks", _allTasks.toString())
                    }

                    for (task in _allTasks ){
                        Log.i("loadedTasks", task.toString())
                        when(task.status){
                            TaskStatus.MISSED.status -> {
                                _missedTasks.add(task)
                            }
                            TaskStatus.ACTIVE.status -> {
                                _activeTasks.add(task)
                            }
                            TaskStatus.FINISHED.status -> {
                                _finishedTasks.add(task)
                            }
                        }
                    }
                    missedTasks.value = _missedTasks
                    activeTasks.value = _activeTasks
                    finishedTasks.value = _finishedTasks

                    isLoading.value = false

                }catch (exception: Exception){
                    isLoading.value = false
                    errorMessage.value = exception.message
                }
                }

    }

    fun initLocalDB() {

                   CoroutineScope(Dispatchers.Main).launch {

                       Log.i("inserterTasks", pref.getFirstAttempt().toString())
                       if (!pref.getFirstAttempt()) {
                       pref.setFirstAttempt(true)
                       isLoading.value = true

                       try {

                       withContext(CoroutineScope(context = Dispatchers.Default).coroutineContext) {
                           val list: MutableList<TaskDto> = ArrayList()
                           val currentTime = System.currentTimeMillis()

                           list.add(
                               TaskDto(
                                   "Exercise",
                                   "Do your morning practice",
                                   "28.10.2020",
                                   "07:00",
                                   TaskStatus.MISSED.status,
                                   5,
                                   currentTime
                               )
                           )
                           list.add(
                               TaskDto(
                                   "Breakfast",
                                   "Have breakfast",
                                   "28.10.2020",
                                   "08:00",
                                   TaskStatus.MISSED.status,
                                   5,
                                   currentTime
                               )
                           )
                           list.add(
                               TaskDto(
                                   "Pets",
                                   "Take care about your pets",
                                   "28.10.2020",
                                   "11:00",
                                   TaskStatus.ACTIVE.status,
                                   5,
                                   currentTime
                               )
                           )
                           list.add(
                               TaskDto(
                                   "Girlfriend",
                                   "Metting with your girlfriend",
                                   "28.10.2020",
                                   "13:00",
                                   TaskStatus.ACTIVE.status,
                                   5,
                                   currentTime
                               )
                           )
                           list.add(
                               TaskDto(
                                   "Reading",
                                   "Read 'Hurry Potter'",
                                   "28.10.2020",
                                   "09:00",
                                   TaskStatus.FINISHED.status,
                                   5,
                                   currentTime
                               )
                           )
                           list.add(
                               TaskDto(
                                   "Shower",
                                   "Take a shower",
                                   "28.10.2020",
                                   "08:30",
                                   TaskStatus.FINISHED.status,
                                   5,
                                   currentTime
                               )
                           )

                           for (item in list){
                               localDB.getTaskDao().addTask(item)
                               Log.i("inserterTasks", item.toString())
                           }
                       }

                           Log.i("inserterTasks", "fuckkk")
                       isLoading.value = false
                           loadTaskList()

                   }catch (exception: Exception){
                       isLoading.value = false
                       errorMessage.value = exception.message
                   }
                   }

       }
    }

    private fun setNotifications(task: TaskDto){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val intent = Intent(context, MyReciever::class.java)
                intent.putExtra(Constants.INTENT_TITLE, task.taskName)
                intent.putExtra(Constants.INTENT_DESCRIPTION, task.description)
                intent.putExtra(Constants.INTENT_ID, task.taskId)
                val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)


                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                if (task.updatedDate == null){
                    val alarmTime = task.updatedDate?:0 + task.frequency * 60L * 60L * 1000L
                    val alarmInterval = task.frequency * 60L * 60L * 1000L

                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        alarmTime,
                        alarmInterval,
                        pendingIntent
                    )
                }else{
                    val alarmTime = task.createdDate + task.frequency * 60L * 60L * 1000L
                    val alarmInterval = task.frequency * 60L * 60L * 1000L

                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        alarmTime,
                        alarmInterval,
                        pendingIntent
                    )

                }

                val sourceFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                val dateAsString = task.date.replace(".", "/")
                Log.i("deformatedToDate", dateAsString)
                val date: Date? = sourceFormat.parse(dateAsString)

                val endDateCal = Calendar.getInstance()
                endDateCal.time = formatDateWithName(task.date)
                val alarmTime =endDateCal.timeInMillis + task.time.substringBefore(":").toLong() * 60L * 60L * 1000L +
                        task.time.substringAfter(":").toLong() * 60L * 1000L

                Log.i("LeftTimeMilliseconds", alarmTime.toString())
                Log.i("LeftTimeMilliseconds", System.currentTimeMillis().toString())
                Log.i("LeftTimeMilliseconds", (alarmTime - System.currentTimeMillis()).toString())


                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    alarmTime,
                    pendingIntent
                )


            }catch (exception: java.lang.Exception){
                errorMessage.value = exception.message
            }
        }
    }
    fun formatDateWithName(date: String): Date? {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return sdf.parse(date)
    }

}