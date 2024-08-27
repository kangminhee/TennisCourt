package com.example.tennis.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

object PlaceholderContent {

    val url = "http://openapi.seoul.go.kr:8088/576e4e6c6779656c36355368686444/json/ListPublicReservationSport/1/300/테니스장"
//    lateinit var jsonObject: JSONObject
    lateinit var rows: JSONArray
//    lateinit var court: JSONObject

    var isLoading: Boolean = false
        private set

    //about all courts
    val ITEMS: MutableList<PlaceholderItem> = ArrayList()
    val ITEM_MAP: MutableMap<String, PlaceholderItem> = HashMap()

    val PLACE_ITEMS: MutableList<PlaceholderItem> = ArrayList()
    val PLACE_ITEM_MAP: MutableMap<String, PlaceholderItem> = HashMap()


    //courts (areanm)
    val uniquePlaces: MutableList<String> = ArrayList()

    fun groupByPlace( place: String ): List<PlaceholderItem> {
        return ITEMS.filter { it.place == place }
    }

    fun importData(onDataLoaded: () -> Unit, onLoadingStatusChanged: (Boolean) -> Unit){
        isLoading = true
        onLoadingStatusChanged(isLoading)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url(url)
                    .build()

                val response = client.newCall(request).execute()
                val jsonData = response.body?.string()

                if (jsonData != null) {
                    val jsonObject = JSONObject(jsonData)
                    rows = jsonObject.getJSONObject("ListPublicReservationSport").getJSONArray("row")
                    parseData()

                    onDataLoaded()
                }
            } catch (e: Exception) {
                Log.e("CourtListView", "Error fetching data: ${e.message}")
            } finally {
                isLoading = false
                onLoadingStatusChanged(isLoading)
            }
        }
    }

    private fun parseData() {
        val placeSet = HashSet<String>()  // 중복 제거를 위한 Set 사용
        for (i in 0 until rows.length()) {
            val court = rows.getJSONObject(i)
            val placeName = court.getString("PLACENM")

            if (placeSet.add(placeName)) {
                uniquePlaces.add(placeName)
                val item = createPlaceholderItem(court)
                addPlaceItem(item)
            }
            val item = createPlaceholderItem(court)
            addItem(item)
        }
    }

    private fun addPlaceItem(item: PlaceholderItem) {
        PLACE_ITEMS.add(item)
        PLACE_ITEM_MAP[item.id] = item
    }

    private fun addItem(item: PlaceholderItem) {
        ITEMS.add(item)
        ITEM_MAP[item.id] = item
    }

    private fun createPlaceholderItem(court: JSONObject): PlaceholderItem {
        return PlaceholderItem(
            court.getString("SVCID"),
            court.getString("PLACENM"),
            court.getString("PAYATNM") == "유료",
            court.getString("DTLCONT"),
            court.getString("AREANM"),
            court.getString("SVCSTATNM") != "예약마감",
            court.getString("SVCNM")
        )
    }

    data class PlaceholderItem(
        val id: String,
        val place: String,
        val payment: Boolean,
        val details: String,
        val area: String,
        val state: Boolean,
        val svcname: String
    )
}