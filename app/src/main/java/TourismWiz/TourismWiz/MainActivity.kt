package TourismWiz.TourismWiz

import City
import TourismWiz.TourismWiz.ui.TourismWizApp
import TourismWiz.TourismWiz.ui.screens.RestaurantViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import TourismWiz.TourismWiz.ui.theme.TourismWizTheme

var token  ="eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJER2lKNFE5bFg4WldFajlNNEE2amFVNm9JOGJVQ3RYWGV6OFdZVzh3ZkhrIn0.eyJleHAiOjE2ODI5MjY3MDMsImlhdCI6MTY4Mjg0MDMwMywianRpIjoiMzIyZWY3NjEtODZhZC00OGNhLTljMDEtOTQ2OTE0NDUwYjYzIiwiaXNzIjoiaHR0cHM6Ly90ZHgudHJhbnNwb3J0ZGF0YS50dy9hdXRoL3JlYWxtcy9URFhDb25uZWN0Iiwic3ViIjoiMzIwZWY4NmMtMGYyZC00N2Y0LWIyNDItNTUwNzIyNWJhMDgxIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiMDA5NTcwNTAtM2IyYWRlYjEtMjA2ZS00NWJjIiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJzdGF0aXN0aWMiLCJwcmVtaXVtIiwibWFhcyIsImFkdmFuY2VkIiwiaGlzdG9yaWNhbCIsImJhc2ljIl19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJ1c2VyIjoiZjZiMGI0ZTkifQ.dnij8xLcvUZ4VM7dE9z562tjh0DEJH7t8k4gIbkLn5QWmgEx0yIs87oqbZBYk9Q4RtTlfxt5j1UOXm_NrtEuMn0Cc3C_Ep40nqYZcizgCh8sv8RhpsVa0DB5-OdSlKUi9V09J3j9Pb-l4OD3V0pHsQKWh7TUcnDUYuiaX5kU6cnFHxuyjEu1T4vzeN1AX0agn5yXsKvklDf_ZlHRSOOAZnZsw72svt7i5kBvotu9S853kA8HkYWyjlDmuoJ9zD8xstvWktRrHI6_F4jlb01rAA8cise8iQwSz1EQ0nt1Sicte_7m4XT2ZiAVkrnFKCyYSTD3LBDkNXvZzTPwqNbJFQ"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TourismWizTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TourismWizApp()
                    /*var headers = mapOf("authorization" to "Bearer $token")
                    var restaurantList = TDXApi.retrofitService.getRestaurants(city = City.miaoliCounty, headers)
                    restaurantList.size.toString()
                    runBlocking {
                        launch {
                            //val restaurantList = TDXApi.retrofitService.getRestaurants(City.keelung)

                            var token  =""
                            var headers = mapOf("authorization" to "Bearer $token")
                            var restaurantList = TDXApi.retrofitService.getRestaurants(city = City.miaoliCounty, headers)
                            Log.w("restaurant",restaurantList.size.toString())

                            /*
                            TDXTokenApi.retrofitService
                                .getUserLogin("client_credentials", "","")
                                .enqueue(object : Callback<TokenResponse> {
                                    override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                                        if (response.isSuccessful) {
                                            val responseData = response.body() // 获取响应数据
                                            Log.w("token",responseData!!.access_token)
                                        } else {
                                            // 处理非成功响应
                                        }
                                    }

                                    override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                                        //TODO: alert user that there is error getting data
                                    }
                                })
                             */
                        }
                    }*/
                }
            }
        }
    }


}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TourismWizTheme {
        Greeting("Android")
    }
}