package com.example.capstone.Main.home.homeModels

data class HomePageResponse(
    val status: Int,
    val data: ResultHomePage,
    val error: Error ?= null
)

