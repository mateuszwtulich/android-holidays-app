package com.example.programowanieaplikacjimultimedialnych.view_model.dto

import android.net.Uri
import java.time.LocalDate
import java.time.Month

class PostDtoOutput (
    var id : Int,
    var title : String,
    var text : String,
    var location : Pair<Double,Double> = Pair(8.0,8.0),
    var date  : LocalDate = LocalDate.of(2016, Month.APRIL, 15),
    var uriList: List<Uri>
)
