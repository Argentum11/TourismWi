import TourismWiz.TourismWiz.model.Restaurant
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.http.*

interface RestaurantApiService {


    @GET("v2/Tourism/Restaurant/{city}?%24top=100&%24format=JSON")
    suspend fun getRestaurants(
        @Path("city") city: String? = "Taoyuan",
        @HeaderMap headers: Map<String, String>
    ): List<Restaurant>
}

//todo config token parameter
//todo config city parameter

object City{
    const val taipei = "Taipei"
    const val newTaipei = "NewTaipei"
    const val taoyuan = "Taoyuan"
    const val taichung = "Taichung"
    const val tainan = "Tainan"
    const val kaohsiung = "Kaohsiung"
    const val keelung = "Keelung"
    const val hsinchu = "Hsinchu"
    const val hsinchuCounty = "HsinchuCounty"
    const val miaoliCounty = "MiaoliCounty"
    const val changhuaCounty = "ChanghuaCounty"
    const val nantouCounty = "NantouCounty"
    const val yunlinCounty = "YunlinCounty"
    const val chiayiCounty = "ChiayiCounty"
    const val chaiyi = "Chiayi"
    const val pingtungCounty = "PingtungCounty"
    const val yilanCounty = "YilanCounty"
    const val hualienCounty = "HualienCounty"
    const val taitungCounty = "TaitungCounty"
    const val kinmenCounty = "KinmenCounty"
    const val penghuCounty = "PenghuCounty"
    const val lienchiangCounty = "LienchiangCounty"
}

//Delete if success

private const val BASE_URL =
    "https://tdx.transportdata.tw/api/basic/"
//private const
private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
    .baseUrl(BASE_URL)
    .build()
object TDXApi {
    val retrofitService : RestaurantApiService by lazy {
       retrofit.create(RestaurantApiService::class.java)
    }
}