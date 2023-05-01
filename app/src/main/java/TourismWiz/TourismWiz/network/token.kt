package TourismWiz.TourismWiz.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


private const val BASE_URL =
    "https://tdx.transportdata.tw/auth/realms/TDXConnect/protocol/openid-connect/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

data class TokenResponse(
    var access_token: String,
    var expires_in: Int = 0,
    var refresh_expires_in: Int,
    var token_type: String,
    var not_before_policy: Int,
    var scope: String
)

interface TokenService{
    @FormUrlEncoded
    @POST("token/")
    fun getUserLogin(
        @Field("grant_type") uname: String,
        @Field("client_id") id: String,
        @Field("client_secret") secret: String
    ): Call<TokenResponse>
}

object TDXTokenApi {
    val retrofitService : TokenService by lazy {
        retrofit.create(TokenService::class.java)
    }
}