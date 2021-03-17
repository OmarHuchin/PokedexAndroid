package com.example.pokedex.data.local.database

class DataBaseConstants {
    companion object{
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "pokdex.db"

        const val TABLE_NAME_POKEMONS = "rooms"

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_ORDER = "order"
        const val COLUMN_WEIGHT = "weight"
        const val COLUMN_URL = "url"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_FRONT_IMAGE = "front_image"
    }
}