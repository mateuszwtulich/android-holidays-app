package com.example.programowanieaplikacjimultimedialnych.controller_ui

import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.programowanieaplikacjimultimedialnych.R
import com.example.programowanieaplikacjimultimedialnych.view_model.HolidayViewModel
import com.example.programowanieaplikacjimultimedialnych.view_model.dto.PostDtoOutput
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places

class LocationsOnMapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var markerList = mutableListOf<Marker>()
    private var holidayViewModel = HolidayViewModel(application = Application())
    private lateinit var markerOptions: MarkerOptions
    private var postList = mutableListOf<PostDtoOutput>()

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val PLACE_PICKER_REQUEST = 3
        private const val ZOOM = 8f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.google_maps_key))
        }
        setContentView(R.layout.activity_locations_on_map)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.locations_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun setUpMap() {

        if(intent.hasExtra("markerOptions")){
            val markerOptions = intent.getParcelableExtra("markerOptions") as MarkerOptions
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(markerOptions.position, ZOOM))
        }

        postList = intent.getParcelableArrayListExtra<PostDtoOutput>("postsList")

        postList.forEach { post ->
            val latLng = LatLng(post.location.latitude, post.location.longitude)
            val markerOptions = MarkerOptions().position(latLng).title(post.title)
            markerList.add(map.addMarker(markerOptions))
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
            )

        }
        map.isMyLocationEnabled = true
        map.mapType = GoogleMap.MAP_TYPE_NORMAL

    }

    override fun onMarkerClick(p0: Marker?): Boolean = false

    private fun getLatLng(location: String): LatLng {
        val geocoder = Geocoder(this)
        val adresses: List<Address> = geocoder.getFromLocationName(location, 1)
        if (adresses.isNotEmpty()) {
            return LatLng(adresses[0].latitude, adresses[0].longitude)
        }
        return LatLng(0.0, 0.0)
    }

    private fun getAddress(location: LatLng): String {
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

        return addresses[0].getAddressLine(0)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)

        setUpMap()
    }
}