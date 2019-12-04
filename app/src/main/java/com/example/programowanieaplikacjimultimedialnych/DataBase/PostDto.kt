package com.example.programowanieaplikacjimultimedialnych.database

import com.example.programowanieaplikacjimultimedialnych.model.MultimediaPath
import java.time.LocalDate
import java.time.Month


//dto - data transfer object
class PostDto (
    var id : Int,
    var title : String,
    var text : String,
    var location : Pair<Double,Double> = Pair(8.0,8.0),
    var date  : LocalDate = LocalDate.of(2016, Month.APRIL, 15),
    var multimediaPaths : List<MultimediaPath> = listOf(MultimediaPath(0,"13",0)))