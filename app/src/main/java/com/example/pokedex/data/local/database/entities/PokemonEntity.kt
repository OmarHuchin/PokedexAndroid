package com.example.pokedex.data.local.database.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.pokedex.data.local.database.DataBaseConstants
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = DataBaseConstants.TABLE_NAME_POKEMONS)
class PokemonEntity() : Parcelable {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = DataBaseConstants.COLUMN_ID)
    var id : Long = 0
    @ColumnInfo(name = DataBaseConstants.COLUMN_NAME)
    var name : String? = ""
    @ColumnInfo(name = DataBaseConstants.COLUMN_ORDER)
    var order : Int = 0
    @ColumnInfo(name = DataBaseConstants.COLUMN_WEIGHT)
    var weight : Int = 0
    @ColumnInfo(name = DataBaseConstants.COLUMN_URL)
    var url: String? = ""
    @ColumnInfo(name = DataBaseConstants.COLUMN_IMAGE)
    var coverImage: String? = ""
    @ColumnInfo(name = DataBaseConstants.COLUMN_FRONT_IMAGE)
    var frontImage: String? = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        name = parcel.readString()
        order = parcel.readInt()
        weight = parcel.readInt()
        url = parcel.readString()
        coverImage = parcel.readString()
        frontImage = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel,flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeInt(order)
        parcel.writeInt(weight)
        parcel.writeString(url)
        parcel.writeString(coverImage)
        parcel.writeString(frontImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PokemonEntity> {
        override fun createFromParcel(parcel: Parcel): PokemonEntity {
            return PokemonEntity(parcel)
        }

        override fun newArray(size: Int): Array<PokemonEntity?> {
            return arrayOfNulls(size)
        }
    }
}
