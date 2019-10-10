package com.example.p3tchild.Model

import kotlinx.serialization.Serializable

@Serializable
class SearchParams {
    var deficience: Boolean = false
    var home: String? = null
    var area: Int? = null
    var time: Int? = null
    var children: Boolean = false
    var allergic: Boolean = false
    var pets: Boolean = false
}