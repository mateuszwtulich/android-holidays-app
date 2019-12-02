package com.example.programowanieaplikacjimultimedialnych

import androidx.lifecycle.LiveData
import androidx.room.ColumnInfo
import com.example.programowanieaplikacjimultimedialnych.Model.Location
import com.example.programowanieaplikacjimultimedialnych.Model.MultimediaPath
import java.util.*

class PostDto (
    var id: Int,
    var title : String,
    var text : String,
    var location : Location,
    var date : Date,
    var multimediaPaths : List<MultimediaPath>)