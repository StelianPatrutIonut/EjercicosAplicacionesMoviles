package com.example.appdefinitiva10

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id:Int,
    @ColumnInfo(name = "username") val userName:String,
    @ColumnInfo(name = "password") val password:String,
    @ColumnInfo(name = "avatar") val avatar: ByteArray?,
    @ColumnInfo(name = "fecha_nacimiento") val fechaNacimiento: Date,

)

