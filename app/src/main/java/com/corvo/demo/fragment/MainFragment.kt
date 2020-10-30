package com.corvo.demo.fragment

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.corvo.demo.R
import com.corvo.demo.adapter.BrandsListener
import com.corvo.demo.adapter.TaskListAdapter
import com.corvo.demo.base.BaseFragment
import com.corvo.demo.db.TaskDto
import com.corvo.demo.helper.CustomLayoutManager
import com.corvo.demo.helper.FrequencyList
import com.corvo.demo.helper.TaskStatus
import com.corvo.demo.viewModel.TaskViewModel
import com.corvo.demo.viewModel.TaskViewModelFactory
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_task_edit_or_add.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class MainFragment : BaseFragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: TaskViewModelFactory by instance()
    private lateinit var viewModel: TaskViewModel

    private var missedTasksAdapter: TaskListAdapter? = null
    private var activeTasksAdapter: TaskListAdapter? = null
    private var finishedTasksAdapter: TaskListAdapter? = null

    private val missedTaskList: MutableList<TaskDto> = ArrayList()
    private val activeTaskList: MutableList<TaskDto> = ArrayList()
    private val finishedTaskList: MutableList<TaskDto> = ArrayList()

    override fun getLayout(): Int {
        return R.layout.fragment_main
    }

    override fun setupViews() {
        viewModel = ViewModelProviders.of(this, factory).get(TaskViewModel::class.java)

        getBaseActivity {

            addTask.setOnClickListener {
                openSingleTaskFragment(null)
            }

            val managerMissedTasks = CustomLayoutManager(it)
            managerMissedTasks.setScrollEnabled(false)

            val managerActiveTasks = CustomLayoutManager(it)
            managerActiveTasks.setScrollEnabled(false)

            val managerFinishedTasks = CustomLayoutManager(it)
            managerFinishedTasks.setScrollEnabled(false)


            missedTasksAdapter = TaskListAdapter(it, missedTaskList, object : BrandsListener {
                override fun onItemClicked(item: TaskDto) {
                    openSingleTaskFragment(item)
                }

                override fun onFinished(item: TaskDto) {
                    item.status = TaskStatus.FINISHED.status
                    viewModel.updateTask(item)
                }
            })

            activeTasksAdapter = TaskListAdapter(it, activeTaskList, object : BrandsListener {
                override fun onItemClicked(item: TaskDto) {
                    openSingleTaskFragment(item)
                }

                override fun onFinished(item: TaskDto) {
                    item.status = TaskStatus.FINISHED.status
                    viewModel.updateTask(item)
                }
            })

            finishedTasksAdapter = TaskListAdapter(it, finishedTaskList, object : BrandsListener {
                override fun onItemClicked(item: TaskDto) {
                    openSingleTaskFragment(item)
                }

                override fun onFinished(item: TaskDto) {
                }
            })
            missedTasksRecycler.layoutManager = managerMissedTasks
            missedTasksRecycler.isNestedScrollingEnabled = false
            missedTasksRecycler.setHasFixedSize(false)
            missedTasksRecycler.adapter = missedTasksAdapter

            activeTasksRecycler.layoutManager = managerActiveTasks
            activeTasksRecycler.isNestedScrollingEnabled = false
            activeTasksRecycler.setHasFixedSize(false)
            activeTasksRecycler.adapter = activeTasksAdapter

            finishedTasksRecycler.layoutManager = managerFinishedTasks
            finishedTasksRecycler.isNestedScrollingEnabled = false
            finishedTasksRecycler.setHasFixedSize(false)
            finishedTasksRecycler.adapter = finishedTasksAdapter

            setObservers()
            refresher.setOnRefreshListener() {
                viewModel.loadTaskList()
            }

            taskChoiceContainer.setOnClickListener { view ->
                val list = listOf(
                    getString(R.string.all_tasks),
                    getString(R.string.missed_tasks),
                    getString(R.string.active_tasks),
                    getString(R.string.finished_tasks)
                )

                it.showBottomSheetDialog(getString(R.string.choose_frequency), list) { position ->
                    filteredTask.text = list.get(position)
                    when (list.get(position)) {
                        getString(R.string.all_tasks) -> {
                            missedListContainer.visibility = View.VISIBLE
                            activeListContainer.visibility = View.VISIBLE
                            finishedListContainer.visibility = View.VISIBLE
                        }
                        getString(R.string.missed_tasks) -> {
                            missedListContainer.visibility = View.VISIBLE
                            activeListContainer.visibility = View.GONE
                            finishedListContainer.visibility = View.GONE
                        }
                        getString(R.string.active_tasks) -> {
                            missedListContainer.visibility = View.GONE
                            activeListContainer.visibility = View.VISIBLE
                            finishedListContainer.visibility = View.GONE
                        }
                        getString(R.string.finished_tasks) -> {
                            missedListContainer.visibility = View.GONE
                            activeListContainer.visibility = View.GONE
                            finishedListContainer.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun setObservers() {
        getBaseActivity {
            viewModel.taskAdded.observe(this, Observer { msg ->
                it.showSuccessMessage(msg)
                viewModel.loadTaskList()
            })

            viewModel.taskUpdated.observe(this, Observer { msg ->
                it.showSuccessMessage(msg)
                viewModel.loadTaskList()
            })

            viewModel.taskDeleted.observe(this, Observer { msg ->
                it.showSuccessMessage(msg)
                viewModel.loadTaskList()
            })

            viewModel.isLoading.observe(this, Observer { initializing ->
                refresher.isRefreshing = initializing
            })

            viewModel.errorMessage.observe(this, Observer { msg ->
                it.showErrorMessage(msg)
            })

            viewModel.missedTasks.observe(this, Observer { missedTasks ->
                if (missedTasks.isNotEmpty()) missedListContainer.visibility = View.VISIBLE
                else missedListContainer.visibility = View.GONE

                missedTaskList.clear()
                missedTaskList.addAll(missedTasks)
                missedTasksAdapter?.notifyDataSetChanged()
            })

            viewModel.activeTasks.observe(this, Observer { activeTasks ->
                if (activeTasks.isNotEmpty()) activeListContainer.visibility = View.VISIBLE
                else activeListContainer.visibility = View.GONE

                activeTaskList.clear()
                activeTaskList.addAll(activeTasks)
                activeTasksAdapter?.notifyDataSetChanged()
            })

            viewModel.finishedTasks.observe(this, Observer { finishedTasks ->
                if (finishedTasks.isNotEmpty()) finishedListContainer.visibility = View.VISIBLE
                else finishedListContainer.visibility = View.GONE

                finishedTaskList.clear()
                finishedTaskList.addAll(finishedTasks)
                finishedTasksAdapter?.notifyDataSetChanged()
            })
        }

        viewModel.initLocalDB()
        viewModel.loadTaskList()
    }


    override fun getTitle(): String {
        return "MainFragment"
    }

    fun openSingleTaskFragment(item: TaskDto?) {
        getBaseActivity {
            val fragment = TaskAddOrEditFragment()
            fragment.listener = object : ITaskOperation {
                override fun onNewTaskAdded(task: TaskDto) {
                    viewModel.addTask(task)
                }

                override fun onTaskEdited(task: TaskDto) {
                    viewModel.updateTask(task)
                }

                override fun onTaskDeleted(task: TaskDto) {
                    viewModel.deleteTask(task)
                }
            }
            fragment.oldTask = item
            it.pushRightToLeftFragment(R.id.full_screen_container, fragment, "MainFragment")
        }
    }

}