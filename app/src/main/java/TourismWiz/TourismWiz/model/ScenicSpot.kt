package TourismWiz.TourismWiz.model

import kotlinx.serialization.Serializable
@Serializable
data class ScenicSpot(
    val ScenicSpotID: String,
    val ScenicSpotName: String,
    val DescriptionDetail: String,
    val Description: String? = null,
    val Phone: String? = null,
    val Address: String? = null,
    val ZipCode: String? = null,
    val TravelInfo: String? = null,
    val OpenTime: String? = null,
    val Picture: Picture,
    val MapUrl: String? = null,
    val Position: Position? = null,
    val Class1: String? = null,
    val Class2: String? = null,
    val Class3: String? = null,
    val Level: String? = null,
    val WebsiteUrl: String? = null,
    val ParkingInfo: String? = null,
    val ParkingPosition: Position? = null,
    val TicketInfo: String? = null,
    val Remarks: String? = null,
    val Keyword: String? = null,
    val City: String,
    val SrcUpdateTime: String,
    val UpdateTime: String
)