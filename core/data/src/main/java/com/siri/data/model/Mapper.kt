package com.siri.data.model

import com.siri.database.model.MovieLocalModel
import com.siri.network.models.MovieModel

// Diese Funktionen wandeln Datenmodelle um:
//asLocalModel: Konvertiert Netzwerkmodell → Datenbankmodell
//asExternalModel: Konvertiert Datenbankmodell → UI-Model
//asLocalModel: แปลงข้อมูลจาก API ให้เหมาะกับการเก็บใน Room (local)
//asExternalModel: แปลงข้อมูลจาก Room ไปเป็น model ที่ UI ใช้แสดงผล
fun MovieModel.asLocalModel() = MovieLocalModel(
    id = id,
    title = title ?: "",
    cover = backdropPath ?: posterPath ?: "",
)

fun MovieLocalModel.asExternalModel() = Movie(
    id = id,
    title = title,
    category = "",
    cover = cover,
)