package com.example.project2

import java.io.Serializable

data class Metrics (
    val RPM: Double,
    val RemainingFuel: Double,
    // val user: String,    // Currently will just have this and the data on file in the data
    val ambient: Double,
    val Object: Double
    // Date(year: Int, month: Int, day: Int)
): Serializable{
    constructor() : this(0.0, 1.0, 0.0, 0.0)
}