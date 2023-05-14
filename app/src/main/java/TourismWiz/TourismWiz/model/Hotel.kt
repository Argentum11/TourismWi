package TourismWiz.TourismWiz.model

import kotlinx.serialization.Serializable

@Serializable
data class Hotel(
    val HotelID: String,
    val HotelName: String,
    val Description: String? = null,
    val Grade: String? = null,
    val Address: String,
    val ZipCode: String,
    val Phone: String,
    val Fax: String? = null,
    val WebsiteUrl: String? = null,
    val Picture: Picture,
    val Position: Position,
    val Class: String,
    val MapUrl: String? = null,
    val Spec: String? = null,
    val ServiceInfo: String? = null,
    val ParkingInfo: String,
    val City: String,
    val SrcUpdateTime: String,
    val UpdateTime: String
)