package com.example.mytvapplication

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.net.URI
import java.net.URISyntaxException

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var title: String? = null,
    var studio: String? = null,
    var description: String? = null,
    var cardImageUrl: String? = null,
    var category: String? = "Series"
) : Serializable {

    fun getCardImageURI(): URI? {
        return try {
            URI(cardImageUrl)
        } catch (e: URISyntaxException) {
            null
        }
    }

    override fun toString(): String {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", cardImageUrl='" + cardImageUrl + '\'' +
                '}'
    }

    companion object {
        private const val serialVersionUID = 727566175075960653L
    }
}
