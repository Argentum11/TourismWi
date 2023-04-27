import TourismWiz.TourismWiz.network.Picture
import TourismWiz.TourismWiz.network.Position
import kotlinx.serialization.Serializable

@Serializable
data class Hotel (
    val HotelID:String,
    val HotelName:String,
    val Description:String,
    val Address:String,
    val ZipCode:String,
    val Phone:String,
    val Picture:Picture,
    val Position:Position,
    val Class:String,
    val ServiceInfo:String,
    val ParkingInfo:String,
    val SrcUpdateTime:String,
    val UpdateTime:String,
    val Fax:String,
    val City:String,
)