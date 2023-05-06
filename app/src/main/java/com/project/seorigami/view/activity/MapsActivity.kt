package com.project.seorigami.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.Layer
import com.project.seorigami.R
import com.project.seorigami.databinding.ActivityMapsBinding
import com.project.seorigami.util.KeyIntent
import java.util.Locale


class MapsActivity : AppCompatActivity(), PermissionsListener, OnMapReadyCallback {
    private lateinit var binding: ActivityMapsBinding

    private val DROPPED_MARKER_LAYER_ID = "DROPPED_MARKER_LAYER_ID"
    private var mapboxMap: MapboxMap? = null
    private var permissionsManager: PermissionsManager? = null
    private var hoveringMarker: ImageView? = null
    private var droppedMarkerLayer: Layer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager?.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    override fun onExplanationNeeded(p0: MutableList<String>?) {
//        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    override fun onPermissionResult(p0: Boolean) {
        if (p0 && mapboxMap != null) {
            val style = mapboxMap!!.style
            style?.let { enableLocationPlugin(it) }
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG)
                .show()
            finish()
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(Style.MAPBOX_STREETS, object : Style.OnStyleLoaded {
            override fun onStyleLoaded(style: Style) {
                enableLocationPlugin(style);

//                Toast.makeText(this@MapsActivity, getString(R.string.move_map_instruction), Toast.LENGTH_SHORT).show()

                hoveringMarker = ImageView(this@MapsActivity)
                hoveringMarker?.setImageResource(R.drawable.ic_round_location_on_red)
                val params = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER
                )
                hoveringMarker?.layoutParams = params
                binding.mapView.addView(hoveringMarker)

                binding.button.setOnClickListener {
                    var mapTargetLatLng: LatLng = mapboxMap.cameraPosition.target
                    reverseGeocode(Point.fromLngLat(mapTargetLatLng.longitude, mapTargetLatLng.latitude))
                }
            }
        })
    }

    private fun reverseGeocode(point: Point) {
        val geocoder = Geocoder(this, Locale.getDefault())
        var addresses: List<Address>? = emptyList()

        try {
            addresses = geocoder.getFromLocation(point.latitude(), point.longitude(), 1)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (!addresses.isNullOrEmpty()) {
//            Log.e("address", "${addresses[0].getAddressLine(0)}")
//            Log.e("adminArea", "${addresses[0].adminArea}")
//            Log.e("subAdminArea", "${addresses[0].subAdminArea.removePrefix("Kabupaten ").removePrefix("Kota ")}")
//            Log.e("countryName", "${addresses[0].countryName}")
//            Log.e("locality", "${addresses[0].locality}")
//            Log.e("subLocality", "${addresses[0].subLocality}")

            val intent = Intent()
            intent.putExtra(KeyIntent.LAT_LONG_DATA.name, "${point.latitude()}, ${point.longitude()}")
            intent.putExtra(KeyIntent.KOTA_DATA.name, addresses[0].subAdminArea.removePrefix("Kabupaten ").removePrefix("Kota ").removeSuffix(" Regency"))
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationPlugin(loadedMapStyle: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            val locationComponent = mapboxMap!!.locationComponent
            locationComponent.activateLocationComponent(
                LocationComponentActivationOptions.builder(
                    this, loadedMapStyle
                ).build()
            )
            locationComponent.isLocationComponentEnabled = true
            locationComponent.cameraMode = CameraMode.TRACKING
            locationComponent.renderMode = RenderMode.NORMAL
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager!!.requestLocationPermissions(this)
        }
    }
}