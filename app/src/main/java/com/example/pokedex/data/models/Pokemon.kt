package com.example.pokedex.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Pokemon (
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("order") val order : Int,
    @SerializedName("sprites") val sprites : Sprites?,
    @SerializedName("weight") val weight : Int
    )
data class Sprites (
    @SerializedName("back_default") val back_default : String,
    @SerializedName("back_female") val back_female : String,
    @SerializedName("back_shiny") val back_shiny : String,
    @SerializedName("back_shiny_female") val back_shiny_female : String,
    @SerializedName("front_default") val front_default : String,
    @SerializedName("front_female") val front_female : String,
    @SerializedName("front_shiny") val front_shiny : String,
    @SerializedName("front_shiny_female") val front_shiny_female : String,
)
/*
*     @SerializedName("other") val other : Other,
    @SerializedName("versions") val versions : Versions*/
data class PokemonMin(val name:String,val url:String){
}
class PokemonResult{
    var count:Int = 0
    var next: String? = null
    var previous: String? = null
    var results: List<PokemonMin> = listOf()
}