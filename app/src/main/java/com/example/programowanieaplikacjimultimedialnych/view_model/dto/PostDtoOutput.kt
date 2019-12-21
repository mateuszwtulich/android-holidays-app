package com.example.programowanieaplikacjimultimedialnych.view_model.dto

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate
import java.time.Month

@Parcelize
class PostDtoOutput(
    var id : Int,
    var title : String,
    var text : String,
    var location : Pair<Double,Double> = Pair(8.0,8.0),
    var date  : LocalDate = LocalDate.of(2016, Month.APRIL, 15),
    var uriList: List<Uri>
) : Parcelable
