package com.corvo.demo.db

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.sql.Date

@Entity(tableName = "tasks")
data class TaskDto(

    var taskName: String,
    var description: String,
    var date: String,
    var time: String,
    var status: String,
    var frequency: Int,
    var createdDate: Long,
    var updatedDate: Long? = null
){
    @PrimaryKey(autoGenerate = true)
    var taskId: Int? = null
}