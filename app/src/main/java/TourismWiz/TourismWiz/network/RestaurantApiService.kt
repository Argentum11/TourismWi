import TourismWiz.TourismWiz.model.Restaurant
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.http.*
/*
const val token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJER2lKNFE5bFg4WldFajlNNEE2amFVNm9JOGJVQ3RYWGV6OFdZVzh3ZkhrIn0.eyJleHAiOjE2ODMwMDU2MDMsImlhdCI6MTY4MjkxOTIwMywianRpIjoiMjY5Y2FjODQtMWEyMC00ZmIyLTk2NDItMzE1OWUxYjkwZDk0IiwiaXNzIjoiaHR0cHM6Ly90ZHgudHJhbnNwb3J0ZGF0YS50dy9hdXRoL3JlYWxtcy9URFhDb25uZWN0Iiwic3ViIjoiMzIwZWY4NmMtMGYyZC00N2Y0LWIyNDItNTUwNzIyNWJhMDgxIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiMDA5NTcwNTAtM2IyYWRlYjEtMjA2ZS00NWJjIiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJzdGF0aXN0aWMiLCJwcmVtaXVtIiwibWFhcyIsImFkdmFuY2VkIiwiaGlzdG9yaWNhbCIsImJhc2ljIl19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJ1c2VyIjoiZjZiMGI0ZTkifQ.IdyW5BZOqqhuG7vNvvrVOn0KuNzBvb9eXjp83JUSeBilbx6BFuLUK3RnXcGOEFqMdeyn6MHLfLBZGdOX5A5bdkwHzK_IhJYkFgY0eAMDsQxNSaW5rnjVdLU4_eLj5VlR7aL_F1KTHIA3PvRsVTpI5L6_Jby25LJUetVFY0HcegyW5pfmApwnqSKishTvxF5bEMhPPFmCGl1UL1bnGHepoHRgJd9qrQmolASmCtoYvLcGL7kqoW8XYOwiGds5VRyIbBT6wEQBOkpI7krQKa4UJGBxyioqQMn7n_l-ST2dyHHYWERodCv-mBBXhvnfmE8UxVT39ZqTIOcMykiCRFBQJA"
interface TDXApiService {
    @Headers("authorization: Bearer $token")
    @GET("v2/Tourism/Restaurant/NewTaipei?%24top=30&%24format=JSON")
    suspend fun getRestaurants(): String
}*/

//Correct verion
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