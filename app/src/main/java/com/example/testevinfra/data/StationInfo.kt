package com.example.testevinfra

data class StationInfo (
    val id: String,
    val snm: String,
    val op: String,
    val lat: String,
    val lon: String,
    val cl: List<Charger>
    )

data class Charger (
    val cid: String,
    val cst: String,
    val p: String
    )