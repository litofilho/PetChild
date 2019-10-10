package com.example.p3tchild.Model

import kotlinx.serialization.Serializable

@Serializable
class Breed {
    var Aggressiviness: Int? = null
    var Behavior: Int? = null
    var Hypoallergenic: Boolean = false
    var Longevity: Int? = null
    var MaxHeight: Int? = null
    var MaxWeight: Int? = null
    var MinHeight: Int? = null
    var MinWeight: Int? = null
    var Name: String? = null
    var Origin: String? = null
    var Postage: String? = null
    var Resume: String? = null
    var Score: Int? = null
}