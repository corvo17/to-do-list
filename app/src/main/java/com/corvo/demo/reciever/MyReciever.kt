package com.corvo.demo.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.corvo.demo.helper.Constants

class MyReciever: BroadcastReceiver() {
    override fun onReceive(context: Context?, intnt: Intent?) {
        val intent = Intent(context, DemoNotificationService::class.java)

        intent.putExtra(Constants.INTENT_TITLE, intnt?.getStringExtra(Constants.INTENT_TITLE))
        intent.putExtra(Constants.INTENT_DESCRIPTION, intnt?.getStringExtra(Constants.INTENT_DESCRIPTION))
        intent.putExtra(Constants.INTENT_ID, intnt?.getStringExtra(Constants.INTENT_ID))
        Log.i("myLogger", "MyReciever")

        context?.startService(intent)
    }
}