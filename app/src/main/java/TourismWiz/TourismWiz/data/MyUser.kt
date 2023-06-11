package TourismWiz.TourismWiz.data

import com.google.firebase.auth.FirebaseUser

class MyUser {
    companion object {
        var user: FirebaseUser? = null
        var password:String?=null
    }
}