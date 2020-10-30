package com.corvo.demo.reciever

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.corvo.demo.MainActivity
import com.corvo.demo.R
import com.corvo.demo.helper.Constants
import com.corvo.demo.helper.Constants.Companion.TASK_CHANNEL_ID

class DemoNotificationService: Service() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val title = intent?.getStringExtra(Constants.INTENT_TITLE)
        val decription = intent?.getStringExtra(Constants.INTENT_DESCRIPTION)
        val id = intent?.getStringExtra(Constants.INTENT_ID)
        Log.i("myLogger", "DemoNotificationService id = $id")
        Log.i("myLogger", "DemoNotificationService decription = $decription")
        Log.i("myLogger", "DemoNotificationService title = $title")

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(this, TASK_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(decription)
            .setSmallIcon(R.drawable.ic_check)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)


        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

}