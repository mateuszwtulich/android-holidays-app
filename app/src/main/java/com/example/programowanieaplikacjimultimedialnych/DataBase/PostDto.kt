package com.example.programowanieaplikacjimultimedialnych.database

import android.net.Uri
import com.example.programowanieaplikacjimultimedialnych.model.MultimediaPath
import java.time.LocalDate
import java.time.Month


//dto - data transfer object
class PostDto (
    var id : Int,
    var title : String,
    var text : String,
    var location : Pair<Double,Double> = Pair(8.0,8.0),
    var date  : LocalDate = LocalDate.of(2016, Month.APRIL, 15)){
    var uriList: MutableList<Uri> = mutableListOf()

    constructor(id: Int,
                title: String,
                text: String,
                location: Pair<Double, Double> = Pair(8.0,8.0),
                date: LocalDate = LocalDate.of(2016, Month.APRIL, 15),
                multimediaPaths :List<MultimediaPath>) : this(id,title,text,location,date)
    {
         multimediaPaths.forEach {uriList.add(Uri.parse(it.path))}
    }

    constructor(id: Int,
                title: String,
                text: String,
                location: Pair<Double, Double> = Pair(8.0,8.0),
                date: LocalDate = LocalDate.of(2016, Month.APRIL, 15),
                uriList : MutableList<Uri>,boolean: Boolean):this(id,title,text,location,date){
        this.uriList = uriList
    }



}