package com.corvo.demo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.corvo.demo.base.BaseActivity
import com.corvo.demo.db.LocalDB
import com.corvo.demo.helper.Constants.Companion.TASK_CHANNEL_ID
import com.corvo.demo.helper.PrefsUtil
import com.corvo.demo.viewModel.TaskViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import org.kodein.di.generic.bind

class App : Application(), KodeinAware {

    var baseActivity: BaseActivity? = null
    var localDB: LocalDB? = null
    var pref: PrefsUtil? = null
    private val CHANNEL_NAME = "Demo channel"


    override fun onCreate() {
        super.onCreate()
        localDB = LocalDB(this)
        pref = PrefsUtil(this)
        createNotificationCahnnel()
    }

    private fun createNotificationCahnnel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val serviceChannel = NotificationChannel(TASK_CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override val kodein = Kodein.lazy {
        import(androidXModule(this@App))
        bind() from singleton { this }
        bind() from provider {
            TaskViewModelFactory(
                localDB!!, pref!!, this@App
            )
        }
    }
}