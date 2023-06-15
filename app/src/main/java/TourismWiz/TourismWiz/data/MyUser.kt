package TourismWiz.TourismWiz.data

import TourismWiz.TourismWiz.model.Hotel
import TourismWiz.TourismWiz.model.Restaurant
import TourismWiz.TourismWiz.model.ScenicSpot
import com.google.firebase.auth.FirebaseUser

class MyUser {
    companion object {
        var user: FirebaseUser? = null
        var password:String?=null
        var restaurantList:MutableList<Restaurant> = mutableListOf()
        var hotelList:MutableList<Hotel> = mutableListOf()
        var scenicspotList:MutableList<ScenicSpot> = mutableListOf()
    }
}