package TourismWiz.TourismWiz.model

import kotlinx.serialization.Serializable

@Serializable
data class Hotel(
    val HotelID: String,
    val HotelName: String,
    val Description: String,
    val Grade: String,
    val Address: String,
    val ZipCode: String,
    val Phone: String,
    val Fax: String,
    val WebsiteUrl: String,
    val Picture: Picture,
    val Position: Position,
    val Class: String,
    val MapUrl: String,
    val Spec: String,
    val ServiceInfo: String,
    val ParkingInfo: String,
    val City: String,
    val SrcUpdateTime: String,
    val UpdateTime: String
)