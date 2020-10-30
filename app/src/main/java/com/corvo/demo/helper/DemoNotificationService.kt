package com.corvo.demo.helper

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.corvo.demo.MainActivity
import com.corvo.demo.R

class DemoNotificationService: Service() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val title = intent?.getStringExtra(Constants.INTENT_TITLE)
        val decription = intent?.getStringExtra(Constants.INTENT_DESCRIPTION)
        val id = intent?.getStringExtra(Constants.INTENT_ID)

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(this, Constants.TASK_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(decription)
            .setSmallIcon(R.drawable.ic_check)
            .setContentIntent(pendingIntent)
            .build()
        startForeground((id?:"1").toInt(), notification)


        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

}