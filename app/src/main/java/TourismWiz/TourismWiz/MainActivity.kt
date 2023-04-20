package TourismWiz.TourismWiz

import TourismWiz.TourismWiz.network.TDXTokenApi
import TourismWiz.TourismWiz.network.TokenResponse
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import TourismWiz.TourismWiz.ui.theme.TourismWizTheme
import android.util.Log
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
                    Greeting("Android")
                    runBlocking {
                        launch {
                            TDXTokenApi.retrofitService
                                .getUserLogin("client_credentials", "Don't push to Github","Don't push to Github")
                                .enqueue(object : Callback<TokenResponse> {
                                    override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                                        if (response.isSuccessful) {
                                            val responseData = response.body() // 获取响应数据
                                            Log.w("qwer312123",responseData!!.access_token)
                                        } else {
                                            // 处理非成功响应
                                        }
                                    }

                                    override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                                        //TODO: alert user that there is error getting data
                                    }
                                })
                        }
                    }
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