package TourismWiz.TourismWiz.data

import TourismWiz.TourismWiz.R

object City {
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
    const val chiayi = "Chiayi"
    const val pingtungCounty = "PingtungCounty"
    const val yilanCounty = "YilanCounty"
    const val hualienCounty = "HualienCounty"
    const val taitungCounty = "TaitungCounty"
    const val kinmenCounty = "KinmenCounty"
    const val penghuCounty = "PenghuCounty"
    const val lienchiangCounty = "LienchiangCounty"
    val mapEnToUserLanguage = mapOf(
        taipei to R.string.taipei,
        newTaipei to R.string.newTaipei,
        taoyuan to R.string.taoyuan,
        taichung to R.string.taichung,
        tainan to R.string.tainan,
        kaohsiung to R.string.kaohsiung,
        keelung to R.string.keelung,
        hsinchu to R.string.hsinchu,
        hsinchuCounty to R.string.hsinchuCounty,
        miaoliCounty to R.string.miaoliCounty,
        changhuaCounty to R.string.changhuaCounty,
        nantouCounty to R.string.nantouCounty,
        yunlinCounty to R.string.yunlinCounty,
        chiayiCounty to R.string.chiayiCounty,
        chiayi to R.string.chiayi,
        pingtungCounty to R.string.pingtungCounty,
        yilanCounty to R.string.yilanCounty,
        hualienCounty to R.string.hualienCounty,
        taitungCounty to R.string.taitungCounty,
        kinmenCounty to R.string.kinmenCounty,
        penghuCounty to R.string.penghuCounty,
        lienchiangCounty to R.string.lienchiangCounty
    )

    fun getStringId(city: String): Int {
        return mapEnToUserLanguage.get(city) ?: R.string.newTaipei
    }
    const val defaultCity = tainan
    val cities = arrayOf(
        taipei,
        newTaipei,
        taoyuan,
        taichung,
        tainan,
        kaohsiung,
        keelung,
        hsinchu,
        hsinchuCounty,
        miaoliCounty,
        changhuaCounty,
        nantouCounty,
        yunlinCounty,
        chiayiCounty,
        chiayi,
        pingtungCounty,
        yilanCounty,
        hualienCounty,
        taitungCounty,
        kinmenCounty,
        penghuCounty,
        lienchiangCounty
    )
}