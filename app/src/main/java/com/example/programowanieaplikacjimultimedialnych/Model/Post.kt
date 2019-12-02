package com.example.programowanieaplikacjimultimedialnych.Model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "post_table", foreignKeys = arrayOf(ForeignKey(entity = Location::class,
                                                parentColumns = arrayOf("location_id"),
                                                childColumns =  arrayOf("id"),
                                                onDelete = ForeignKey.CASCADE)))
class Post(@PrimaryKey(autoGenerate = true) val id: Int,
           val title : String,
           val text : String,
           @ColumnInfo(name ="location_id") val locationId: Int,
           val date : Date)

