package com.example.tennis.ui.map

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.tennis.R
import com.google.android.gms.maps.model.Marker
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
        mMap.setOnMarkerClickListener { marker ->
            showCourtInfoDialog(marker)
            true
        }
        fetchTennisCourtData()
    }

    private fun startNavigation(destination: LatLng) {
        val uri = Uri.parse("google.navigation:q=${destination.latitude},${destination.longitude}")
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps")

        if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(mapIntent)
        } else {
            Toast.makeText(requireContext(), "Google Maps 앱이 설치되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showCourtInfoDialog(marker: Marker) {
        val courtInfo = marker.tag as? CourtInfo ?: return
        val dialogView = layoutInflater.inflate(R.layout.dialog_court_info, null)

        dialogView.findViewById<TextView>(R.id.tvCourtName).text = courtInfo.name
        dialogView.findViewById<TextView>(R.id.tvCourtPhone).text = courtInfo.phone
        dialogView.findViewById<TextView>(R.id.tvCourtAddress).text = courtInfo.address

        val tvCourtPhone = dialogView.findViewById<TextView>(R.id.tvCourtPhone)
        tvCourtPhone.text = "전화번호: ${courtInfo.phone}"
        tvCourtPhone.setOnClickListener {
            copyToClipboard(courtInfo.phone)
        }

        // 이미지 로딩 (Glide 라이브러리 사용 예시)
        Glide.with(this)
            .load(courtInfo.imgURL)
            .into(dialogView.findViewById<ImageView>(R.id.ivCourtImage))

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("길찾기") { _, _ ->
                startNavigation(courtInfo.latLng)
            }
            .setNegativeButton("닫기", null)
            .show()
    }

    private fun copyToClipboard(text: String) {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("phone number", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "전화번호가 복사되었습니다.", Toast.LENGTH_SHORT).show()
    }

    data class CourtInfo(
        val name: String,
        val phone: String,
        val imgURL: String,
        val address: String,
        val latLng: LatLng
    )

    private fun fetchTennisCourtData() {
        val url = "http://openapi.seoul.go.kr:8088/576e4e6c6779656c36355368686444/json/ListPublicReservationSport/1/300/테니스장"

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
                        val baseUrl = "http://example.com/images/"
                        val imgURL = baseUrl + court.getString("IMGURL")
                        val phone = court.getString("TELNO")
                        val address = court.getString("AREANM") // API에서 주소 정보 가져오기>가 안됨

                        launch(Dispatchers.Main) {
                            val position = LatLng(lat, lng)
                            val marker = mMap.addMarker(MarkerOptions()
                                .position(position)
                                .title(name))

                            marker?.tag = CourtInfo(name, phone, imgURL, address, position)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("MapViewFragment", "Error fetching data: ${e.message}")
            }
        }
    }
}
