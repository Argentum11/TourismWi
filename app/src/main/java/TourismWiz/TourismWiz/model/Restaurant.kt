package TourismWiz.TourismWiz.model

import kotlinx.serialization.Serializable

@Serializable
data class Restaurant(
    val RestaurantID: String,
    val RestaurantName: String,
    val Description: String,
    val Address: String,
    val ZipCode: String? = null,
    val Phone: String,
    val OpenTime: String? = null,
    val WebsiteUrl: String? = null,
    val Picture: Picture? = null,
    val Position: Position? = null,
    val Class: String? = null,
    val MapUrl: String? = null,
    val ParkingInfo: String? = null,
    val City: String,
    val SrcUpdateTime: String,
    val UpdateTime: String
)