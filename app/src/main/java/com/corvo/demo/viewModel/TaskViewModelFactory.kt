package com.corvo.demo.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.corvo.demo.db.LocalDB
import com.corvo.demo.helper.PrefsUtil

@Suppress("UNCHECKED_CAST")
class TaskViewModelFactory(
    private val localDB: LocalDB,
    private val pref: PrefsUtil,
    private val context: Context

) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TaskViewModel(
            localDB,
            pref,
            context
        ) as T
    }
}