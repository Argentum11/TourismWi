package TourismWiz.TourismWiz.model

import kotlinx.serialization.Serializable

@Serializable
data class Picture(
    val PictureUrl1: String? = null,
    val PictureDescription1: String? = null,
    val PictureUrl2: String? = null,
    val PictureDescription2: String? = null,
    val PictureUrl3: String? = null,
    val PictureDescription3: String? = null
)