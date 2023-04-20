import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val BASE_URL =
   "https://tdx.transportdata.tw/api/basic/"
private const val token = ""

private val retrofit = Retrofit.Builder()
   .addConverterFactory(ScalarsConverterFactory.create())
   .baseUrl(BASE_URL)
   .build()

interface TDXApiService{
    @Headers("authorization: Bearer $token")
    @GET("v2/Tourism/Restaurant/NewTaipei?%24top=30&%24format=JSON")
    suspend fun getRestaurants():String
}

object TDXApi {
    val retrofitService : TDXApiService by lazy {
       retrofit.create(TDXApiService::class.java)
    }
}