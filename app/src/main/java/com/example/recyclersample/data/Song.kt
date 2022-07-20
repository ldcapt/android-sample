package com.example.recyclersample.data

import java.io.Serializable

data class Song (
    val title : String,
    val image : Int,
    val singer : String,
    val resource : Int
) : Serializable