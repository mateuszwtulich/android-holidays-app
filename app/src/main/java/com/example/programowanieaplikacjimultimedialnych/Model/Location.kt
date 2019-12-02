package com.example.programowanieaplikacjimultimedialnych.Model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "location_table")
class Location(@PrimaryKey(autoGenerate = true) val id: Int,
               val attitude  : Double,
               val latitude : Double)
