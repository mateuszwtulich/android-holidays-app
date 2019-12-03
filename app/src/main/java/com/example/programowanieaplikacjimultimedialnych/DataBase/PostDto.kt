package com.example.programowanieaplikacjimultimedialnych.DataBase

import androidx.lifecycle.LiveData
import androidx.room.ColumnInfo
import com.example.programowanieaplikacjimultimedialnych.Model.Location
import com.example.programowanieaplikacjimultimedialnych.Model.MultimediaPath
import java.util.*

//dto - data transfer object
class PostDto (
    var id : Int,
    var title : String,
    var text : String,
    var location : Location = Location(0,12.0,12.0),
    var date : Date = Date(),
    var multimediaPaths : List<MultimediaPath> = listOf(MultimediaPath(0,"13",0)))