package com.example.programowanieaplikacjimultimedialnych.view_model.dto

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate
import java.time.Month

@Parcelize
class PostDtoOutput(
    val id : Int,
    val title : String,
    val text : String,
    val location : Location,
    val date  : LocalDate,
    val uriList: List<Uri>
) : Parcelable
