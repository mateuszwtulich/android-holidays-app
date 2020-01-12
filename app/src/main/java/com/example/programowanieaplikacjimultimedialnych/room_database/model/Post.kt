package com.example.programowanieaplikacjimultimedialnych.room_database.model

import androidx.room.*


@Entity(tableName = "post_table")
class Post(@PrimaryKey(autoGenerate = true) val id: Int,
           val title : String,
           val text : String,
           val date : String,
           val latitude : Double,
           val longitude : Double)

