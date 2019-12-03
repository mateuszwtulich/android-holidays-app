package com.example.programowanieaplikacjimultimedialnych.Model

import androidx.room.*


@Entity(tableName = "post_table", foreignKeys = arrayOf(ForeignKey(entity = Location::class,
                                                parentColumns = arrayOf("id"),
                                                childColumns =  arrayOf("location_id"),
                                                onDelete = ForeignKey.CASCADE)))
class Post(@PrimaryKey(autoGenerate = true) val id: Int,
           val title : String,
           val text : String,
           @ColumnInfo(name ="location_id" , index = true) val locationId: Int,
           val date : String)

