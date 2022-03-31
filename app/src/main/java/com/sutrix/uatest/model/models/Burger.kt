package com.sutrix.uatest.model.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "burger_table")
@Serializable
data class Burger(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val ref: String,
    val title: String,
    val description: String,
    val thumbnail: String,
    val price: Double
)


