package com.example.project2

import java.io.Serializable

data class Metrics (
    val speed: Double,
    val altitude: Double,
    val user: String,    // Currently will just have this and the data on file in the data
    val latitude: Double,
    val longitude: Double
    // Date(year: Int, month: Int, day: Int)
): Serializable{
    constructor() : this(0.0, 0.0, "", 0.0, 0.0)
}