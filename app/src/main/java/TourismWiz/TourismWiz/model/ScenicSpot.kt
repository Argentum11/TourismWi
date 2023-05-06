package TourismWiz.TourismWiz.model

@kotlinx.serialization.Serializable
data class ScenicSpot(
    val ScenicSpotID: String,
    val ScenicSpotName: String,
    val DescriptionDetail: String,
    val Description: String,
    val Phone: String,
    val Address: String,
    val ZipCode: String,
    val TravelInfo: String,
    val OpenTime: String,
    val Picture: Picture,
    val MapUrl: String,
    val Position: Position,
    val Class1: String,
    val Class2: String,
    val Class3: String,
    val Level: String,
    val WebsiteUrl: String,
    val ParkingInfo: String,
    val ParkingPosition: Position,
    val TicketInfo: String,
    val Remarks: String,
    val Keyword: String,
    val City: String,
    val SrcUpdateTime: String,
    val UpdateTime: String
)