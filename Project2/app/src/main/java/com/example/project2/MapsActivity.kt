package com.example.project2

import android.location.Address
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val intent = getIntent()
        val breadCrumbs = intent.getParcelableArrayListExtra<LatLng>("breadcrumbs")!!
        val language: Boolean = intent.getBooleanExtra("language", false)

        var startString = getString(R.string.start_location)
        var endString: String = getString(R.string.end_location)
        if(language){
            startString = getString(R.string.start_location_spanish)
            endString = getString(R.string.end_location_spanish)
        }


        val startLocation = breadCrumbs[0]
        mMap.addMarker(MarkerOptions().position(startLocation).title(startString))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startLocation))


        for(i in 1..99){
            googleMap.addPolyline(PolylineOptions().add(breadCrumbs[i-1], breadCrumbs[i]))
        }

        val endLocation = breadCrumbs[breadCrumbs.lastIndex]
        mMap.addMarker(MarkerOptions().position(endLocation).title(endString))
        /*
        var polyline2 = googleMap.addPolyline(PolylineOptions().clickable(false).add(
            startLocation,
            endLocation))
            */
    }
}