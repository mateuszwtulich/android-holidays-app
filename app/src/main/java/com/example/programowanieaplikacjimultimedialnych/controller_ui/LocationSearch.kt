package com.example.programowanieaplikacjimultimedialnych.controller_ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.wifi.WifiConfiguration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewParent
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.example.programowanieaplikacjimultimedialnych.R
import com.google.android.gms.common.api.Status

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.*
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.mancj.materialsearchbar.MaterialSearchBar
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter
import kotlinx.android.synthetic.main.activity_location_search.*
import kotlinx.android.synthetic.main.activity_location_search.view.*
import java.io.IOException
import java.lang.Math.abs
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.collections.ArrayList

class LocationSearch : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private var markerList = mutableListOf<Marker>()
    private lateinit var placesClient: PlacesClient
    private lateinit var predictionList: MutableList<AutocompletePrediction>
    private lateinit var markerOptions: MarkerOptions

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
        setContentView(R.layout.activity_location_search)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val mapView = mapFragment.view
        fab_adding_location.hide()

        val locationButton =
            (mapView!!.findViewById<View>(Integer.parseInt("1")).parent as View).findViewById<View>(Integer.parseInt("2"))
        val rlp = locationButton.layoutParams as (RelativeLayout.LayoutParams)
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        rlp.setMargins(0, 0, 30, 300)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        searchPlace.placeHolderView.ellipsize = TextUtils.TruncateAt.END
        searchPlace.placeHolderView.setTypeface(null, Typeface.NORMAL)
        searchPlace.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
            override fun onSearchStateChanged(enabled: Boolean) {

            }

            override fun onSearchConfirmed(text: CharSequence?) {
                startSearch(text.toString(), true, null, true)
            }

            override fun onButtonClicked(buttonCode: Int) {
                when (buttonCode) {
                    MaterialSearchBar.BUTTON_BACK -> searchPlace.disableSearch()
                }
            }

        })

        val bounds = RectangularBounds.newInstance(LatLng(-33.880490, 151.184363), LatLng(-33.880490, 151.184363))

        searchPlace.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                searchPlace.clearSuggestions()
                placesClient = Places.createClient(baseContext)
                val token = AutocompleteSessionToken.newInstance()
                val predictionsRequest = FindAutocompletePredictionsRequest.builder().setLocationBias(bounds)
                    .setTypeFilter(TypeFilter.CITIES)
                    .setSessionToken(token)
                    .setQuery(charSequence.toString()).build()
                var answer = "0"

                placesClient.findAutocompletePredictions(predictionsRequest)
                    .addOnCompleteListener(object : OnCompleteListener<FindAutocompletePredictionsResponse> {

                        override fun onComplete(task: Task<FindAutocompletePredictionsResponse>) {
                            predictionList = task.getResult()!!.autocompletePredictions

                            searchPlace.updateLastSuggestions(predictionList.map { prediction ->
                                prediction.getFullText(null).toString()
                            })
                            searchPlace.showSuggestionsList()
                            if (predictionList.size > 0) {
                                answer = predictionList[0].toString()
                            }
                        }
                    })
                Toast.makeText(baseContext, "${answer}", Toast.LENGTH_SHORT).show()

            }

            override fun afterTextChanged(editable: Editable) {
            }
        })
        searchPlace.setSuggestionsClickListener(object : SuggestionsAdapter.OnItemViewClickListener {
            override fun OnItemClickListener(position: Int, v: View?) {
                val suggestion: String = searchPlace.lastSuggestions.get(position).toString()
                placeMarkerOnMap(getLatLng(suggestion))
                searchPlace.text = suggestion
                searchPlace.clearSuggestions()
            }

            override fun OnItemDeleteListener(position: Int, v: View?) {
                val newPredictionList = ArrayList<AutocompletePrediction>()
                newPredictionList.addAll(predictionList)
                newPredictionList.removeAt(position)
                searchPlace.updateLastSuggestions(newPredictionList)
            }
        })
    }

    private fun setUpMap() {
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

        if(intent.hasExtra("markerOptions")){
            val markerOptions = intent.getParcelableExtra("markerOptions") as MarkerOptions
            placeMarkerOnMap(markerOptions.position)
        }

        map.isMyLocationEnabled = true
        map.mapType = GoogleMap.MAP_TYPE_NORMAL

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                if(!intent.hasExtra("markerOptions")) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, ZOOM))
                }
            }
        }


    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        fab_adding_location.show()
        fab_adding_location.setOnClickListener {
            intent.putExtra("localization", markerOptions)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        return false
    }

    private fun placeMarkerOnMap(location: LatLng) {
        markerOptions = MarkerOptions().position(location).title(getAddress(location))
        markerList.add(map.addMarker(markerOptions))
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, ZOOM))
    }

    private fun getLatLng(location: String): LatLng {
        val geocoder = Geocoder(this)
        val adresses: List<Address> = geocoder.getFromLocationName(location, 1)
        if (adresses.size > 0) {
            return LatLng(adresses.get(0).latitude, adresses.get(0).longitude)
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
        map.getUiSettings().setZoomControlsEnabled(true)
        map.setOnMarkerClickListener(this)

        map.setOnMapClickListener {
            fab_adding_location.hide()
            placeMarkerOnMap(it)
        }

        map.setOnMapLongClickListener(object : GoogleMap.OnMapLongClickListener {
            override fun onMapLongClick(latLng: LatLng) {
                markerList.forEach {
                    if(abs(it.position.latitude - latLng.latitude) < 0.02 &&
                                abs(it.position.longitude - latLng.longitude) < 0.02)
                        it.remove()
                }

                markerList.removeIf {
                    (abs(it.position.latitude - latLng.latitude) < 0.02 &&
                            abs(it.position.longitude - latLng.longitude) < 0.02)
                }
            }
        })

        setUpMap()
    }
}
