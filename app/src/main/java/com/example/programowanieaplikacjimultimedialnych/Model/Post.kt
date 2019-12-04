package com.example.programowanieaplikacjimultimedialnych.model

import androidx.room.*


@Entity(tableName = "post_table")
class Post(@PrimaryKey(autoGenerate = true) val id: Int,
           val title : String,
           val text : String,
           val date : String,
           val attitude  : Double,
           val latitude : Double)

