package com.example.pokedex.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.pokedex.data.local.database.DataBaseConstants
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Pokemon (
        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = DataBaseConstants.COLUMN_ID)
        @SerializedName("id") var id : Int,
        @ColumnInfo(name = DataBaseConstants.COLUMN_NAME)
        @SerializedName("name") var name : String,
        @ColumnInfo(name = DataBaseConstants.COLUMN_ORDER)
        @SerializedName("order") var order : Int,
        @Ignore
        @SerializedName("sprites") var sprites : Sprites?,
        @ColumnInfo(name = DataBaseConstants.COLUMN_WEIGHT)
        @SerializedName("weight") var weight : Int,
        @ColumnInfo(name = DataBaseConstants.COLUMN_URL)
        @Expose var url: String,
        @ColumnInfo(name = DataBaseConstants.COLUMN_IMAGE)
        @Expose var coverImage: String,
        @ColumnInfo(name = DataBaseConstants.COLUMN_FRONT_IMAGE)
        @Expose var frontImage: String
    )
data class Sprites (
    @SerializedName("back_default") var back_default : String,
    @SerializedName("back_female") var back_female : String,
    @SerializedName("back_shiny") var back_shiny : String,
    @SerializedName("back_shiny_female") var back_shiny_female : String,
    @SerializedName("front_default") var front_default : String,
    @SerializedName("front_female") var front_female : String,
    @SerializedName("front_shiny") var front_shiny : String,
    @SerializedName("front_shiny_female") var front_shiny_female : String,
)
/*
*     @SerializedName("other") var other : Other,
    @SerializedName("versions") var versions : Versions*/
data class PokemonMin(var name:String,var url:String){
}
class PokemonResult{
    var count:Int = 0
    var next: String? = null
    var previous: String? = null
    var results: List<PokemonMin> = listOf()
}