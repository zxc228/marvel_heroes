package dev.ilya.marvelheroes.features.hero

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "heroes")
data class Hero(
    @PrimaryKey
    val id :Int,
    val name :String,
    @Embedded
    val biography :Biography,
    @Embedded
    val images :Images,
    @Embedded
    val powerstats :Stats
) {
    data class Images(
        val md :String,
        val lg :String
    )

    data class Biography(
        val fullName :String,
        val alterEgos :String,
        val placeOfBirth :String,
        val publisher :String,
        val alignment :String
    ) {
        fun toText() :String =
            "fullname: $fullName\n" +
            "alter egos: $alterEgos\n" +
            "place of birth: $placeOfBirth\n" +
            "publisher: $publisher\n" +
            "alignment: $alignment"
    }

    data class Stats(
        val intelligence : Int,
        val strength : Int,
        val speed : Int,
        val durability : Int,
        val power : Int,
        val combat : Int
    )
}