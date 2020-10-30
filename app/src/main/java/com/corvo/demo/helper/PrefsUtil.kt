package com.corvo.demo.helper

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.corvo.demo.App
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class PrefsUtil(private val context : Context): CoroutineScope {
    private val job = Job()
    override val coroutineContext = job + Main
    private val TAG = "RrefsUtil"

    private val IS_FIRST_TIME = "isFirstTime"

    val app = context.applicationContext as App
    val db = app.localDB
    private val sharedPref: SharedPreferences = context.getSharedPreferences("User", 0)

    fun setFirstAttempt(value : Boolean) {
        setBoolean(IS_FIRST_TIME,value)
    }
    fun getFirstAttempt() : Boolean {
        return getBoolean(IS_FIRST_TIME)
    }


    fun clearAll(){
        sharedPref.edit().clear().apply()
    }

    private fun setLong(key : String, value : Long){
        sharedPref.edit().putLong(key,value).apply()
    }

    private fun getLong(key : String) : Long {
        return sharedPref.getLong(key,0) ?: 0
    }



    private fun setString(key : String, value : String){
        sharedPref.edit().putString(key,value).apply()
    }

    private fun getStrings(key : String) : String {
        return sharedPref.getString(key,"") ?: ""
    }


    private fun setStringNullable(key : String, value : String?){
        sharedPref.edit().putString(key,value?:"").apply()
    }

    private fun getStringNullable(key : String) : String? {
        val stringNull =  sharedPref.getString(key,"")
        return if (stringNull.isNullOrEmpty()){
            null
        }else{
            stringNull
        }
    }


    private fun setBoolean(key : String, value : Boolean){
        sharedPref.edit().putBoolean(key,value).apply()
    }

    private fun getBoolean(key : String) : Boolean {
        return sharedPref.getBoolean(key, false)
    }


}