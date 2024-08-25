package com.example.tennis.ui.map

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.tennis.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class MapViewFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_map_view, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return rootView
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val seoul = LatLng(37.5665, 126.9780)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 12f))

        fetchTennisCourtData()
    }

    private fun fetchTennisCourtData() {
        val url = "http://openapi.seoul.go.kr:8088/576e4e6c6779656c36355368686444/json/ListPublicReservationSport/1/200/테니스장"

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
                    val rows = jsonObject.getJSONObject("ListPublicReservationSport").getJSONArray("row")

                    for (i in 0 until rows.length()) {
                        val court = rows.getJSONObject(i)
                        val name = court.getString("PLACENM")
                        val lat = court.getDouble("Y")
                        val lng = court.getDouble("X")
                        val imgURL = court.getString("IMGURL")
                        val phone = court.getString("TELNO")

                        val snippet = "전화번호: $phone\n이미지: $imgURL"

                        launch(Dispatchers.Main) {
                            val position = LatLng(lat, lng)
                            mMap.addMarker(MarkerOptions()
                                .position(position)
                                .title(name)
                                .snippet(snippet))
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("MapViewFragment", "Error fetching data: ${e.message}")
            }
        }
    }
}
