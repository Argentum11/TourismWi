package TourismWiz.TourismWiz.model

import kotlinx.serialization.Serializable

@Serializable
data class Position(
    val PositionLon:Double? = null,
    val PositionLat:Double? = null,
    val GeoHash:String? = null)