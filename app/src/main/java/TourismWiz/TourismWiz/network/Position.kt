package TourismWiz.TourismWiz.network

import kotlinx.serialization.Serializable

@Serializable
data class Position(
    val PositionLon:Double,
    val PositionLat:Double,
    val GeoHash:String)