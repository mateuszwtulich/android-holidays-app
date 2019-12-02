package com.example.programowanieaplikacjimultimedialnych.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey


@Entity(tableName = "multimediaPath_table",foreignKeys = arrayOf(ForeignKey(entity = Location::class,
                                            parentColumns = arrayOf("post_id"),
                                            childColumns =  arrayOf("id"),
                                            onDelete = ForeignKey.CASCADE)))
class MultimediaPath(@PrimaryKey(autoGenerate = true) val id: Int,
                     val path : String,
                     @ColumnInfo(name ="post_id") val postId : Int )

