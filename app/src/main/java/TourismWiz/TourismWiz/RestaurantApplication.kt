package TourismWiz.TourismWiz

import TourismWiz.TourismWiz.data.AppContainer
import TourismWiz.TourismWiz.data.DefaultAppContainer
import android.app.Application

class RestaurantApplication : Application(){
    var container: AppContainer= DefaultAppContainer()
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}