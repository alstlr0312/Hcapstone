package com.unity.mynativeapp.features.ar

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.unity.mynativeapp.R
import java.io.IOException
import java.util.*


class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private val LOCATION_PERMISSTION_REQUEST_CODE: Int = 1000
    private lateinit var locationSource: FusedLocationSource // 위치를 반환하는 구현체
    private lateinit var naverMap: NaverMap
    private val viewModel by viewModels<MapModel>()
    private var markers = ArrayList<MapModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.Walkmap_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSTION_REQUEST_CODE)
        Log.d("현재위치",locationSource.toString())
    }


    override fun onMapReady(@NonNull naverMap: NaverMap) {
        this.naverMap = naverMap

        naverMap.locationSource = locationSource //현재 위치 사용
        naverMap.locationTrackingMode = LocationTrackingMode.NoFollow //위치 변해도 지도 안움직임
        naverMap.uiSettings.isLocationButtonEnabled = true //현재 위치 버튼 활성화LastLocation().toString())
        val lat = naverMap.cameraPosition.target.latitude
        val lon = naverMap.cameraPosition.target.longitude
        val address = getAddress(lat,lon)
        var addressarr = address.split(" ")
        Log.d("lat",lat.toString())
        Log.d("lon",lon.toString())
        Log.d("getAddress(lat,lon)",getAddress(lat,lon))
        Log.d("getAddress(lat,lon)", addressarr[2])
        var roadAdd:String=""
        if(addressarr[2]=="종로구"){
            roadAdd="LOCALDATA_103701_JN"
        } else if(addressarr[2]=="도봉구"){
            roadAdd="LOCALDATA_103701_DB"
        }else if(addressarr[2]=="송파구"){
            roadAdd="LOCALDATA_103701_SP"
        }else if(addressarr[2]=="구로구"){
            roadAdd="LOCALDATA_103701_GR"
        }else if(addressarr[2]=="중랑구"){
            roadAdd="LOCALDATA_103701_JR"
        }else if(addressarr[2]=="성동구"){
            roadAdd="LOCALDATA_103701_SD"
        }else if(addressarr[2]=="강동구"){
            roadAdd="LOCALDATA_103701_GD"
        }else if(addressarr[2]=="동대문구"){
            roadAdd="LOCALDATA_103701_DD"
        }else if(addressarr[2]=="용산구"){
            roadAdd="LOCALDATA_103701_YS"
        }else if(addressarr[2]=="강낭구"){
            roadAdd="LOCALDATA_103701_GN"
        }else if(addressarr[2]=="강서구"){
            roadAdd="LOCALDATA_103701_GS"
        }else if(addressarr[2]=="영등포"){
            roadAdd="LOCALDATA_103701_YD"
        }else if(addressarr[2]=="노원구"){
            roadAdd="LOCALDATA_103701_NW"
        }else if(addressarr[2]=="동작구"){
            roadAdd="LOCALDATA_103701_DJ"
        }else if(addressarr[2]=="은평구"){
            roadAdd="LOCALDATA_103701_EP"
        }else if(addressarr[2]=="관악구"){
            roadAdd="LOCALDATA_103701_GA"
        }else if(addressarr[2]=="양천구"){
            roadAdd="LOCALDATA_103701_YC"
        }else if(addressarr[2]=="성북구"){
            roadAdd="LOCALDATA_103701_SB"
        }else if(addressarr[2]=="마포구"){
            roadAdd="LOCALDATA_103701_MP"
        }else if(addressarr[2]=="서대문구"){
            roadAdd="LOCALDATA_103701_SM"
        }else if(addressarr[2]=="금천구"){
            roadAdd="LOCALDATA_103701_GC"
        }else if(addressarr[2]=="중구"){
            roadAdd="LOCALDATA_103701_JG"
        }else if(addressarr[2]=="광진구"){
            roadAdd="LOCALDATA_103701_GJ"
        }else if(addressarr[2]=="서초구"){
            roadAdd="LOCALDATA_103701_SC"
        }else if(addressarr[2]=="강북구"){
            roadAdd="LOCALDATA_103701_GB"
        }
        Log.d("구",roadAdd)
        viewModel.GetMap(roadAdd,1,1000)
        subscribeUI()

    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    //좌표 주소변환
    private fun getAddress(lat: Double, lng: Double): String {
        val geoCoder = Geocoder(applicationContext, Locale.KOREA)
        val address: ArrayList<Address>
        var addressResult = "주소를 가져 올 수 없습니다."
        try {
            //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
            //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
            address = geoCoder.getFromLocation(lat, lng, 1) as ArrayList<Address>
            if (address.size > 0) {
                // 주소 받아오기
                val currentLocationAddress = address[0].getAddressLine(0)
                    .toString()
                addressResult = currentLocationAddress
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressResult
    }
    //주소 좌표 변환
    fun getLocationFromAddress(strAddress: String): LatLng? {
        val geoCoder = Geocoder(applicationContext, Locale.KOREA)
        val address: List<Address>?
        var p1: LatLng? = null

        try {
            address = geoCoder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }
            val location: Address = address[0]
            location.latitude
            location.longitude

            p1 = LatLng(location.latitude, location.longitude)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return p1
    }

    private fun subscribeUI() {

        viewModel.mapData.observe(this) { data ->
            if (data != null) {  // 다이어리 목록 없음
                val getjgitemInfo = data.LOCALDATA_103701_SP.row
                var name = 0
                for (x in getjgitemInfo) {
                    val Address = x.SITEWHLADDR
                    val status = x.DTLSTATENM
                    val name = x.BPLCNM
                    val location = getLocationFromAddress(Address.toString())
                    Log.d("좌표",getLocationFromAddress(Address.toString()).toString())

                    addMark(location, Address,status, name)
                }
            }

        }

    }

    private fun addMark(location: LatLng?, Address: String?, status: String?, name: String?) {
        val marker = Marker()
        //원근감 표시
        marker.isIconPerspectiveEnabled = true
        //아이콘 지정
        //marker.icon = OverlayImage.fromResource(resourceID)
        //마커의 투명도
        marker.alpha = 0.8f
        //마커 위치
        if (location != null) {
            marker.position = location
        }
        marker.captionText = Address.toString()
        //마커 우선순위
        marker.zIndex = 10

        //마커 표시
        marker.map = naverMap

        marker.setOnClickListener(object : Overlay.OnClickListener {
            override fun onClick(@NonNull overlay: Overlay): Boolean {
                val bottomSheet = MapBottomSheetFragment(this)
                bottomSheet.address = Address.toString()
                bottomSheet.status = status.toString()
                bottomSheet.name = name.toString()
                bottomSheet.show(supportFragmentManager, bottomSheet.tag)
                return false
            }
        })

    }
}
