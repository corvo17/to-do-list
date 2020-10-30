package com.corvo.demo.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyReciever: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        val intent = Intent(p0, DemoNotificationService::class.java)
        intent.putExtra(Constants.INTENT_TITLE, p1?.getStringExtra(Constants.INTENT_TITLE))
        intent.putExtra(Constants.INTENT_DESCRIPTION, p1?.getStringExtra(Constants.INTENT_DESCRIPTION))
        intent.putExtra(Constants.INTENT_ID, p1?.getStringExtra(Constants.INTENT_ID))
        p0?.startService(intent)
    }
}