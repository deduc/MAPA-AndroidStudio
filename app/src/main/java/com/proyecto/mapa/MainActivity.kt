package com.proyecto.mapa

import android.location.Location
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.ProgressBar
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.proyecto.mapa.R
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay

class MainActivity : ComponentActivity() {
    private val myMap: MyMap = MyMap()
    private val permisosHandler = PermisosHandler(this)
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        progressBar = findViewById(R.id.progressBar)
        myMap.mapView = findViewById(R.id.mapView)

        handleMapCreation()
    }

    fun getUserLocation(view: View) {
        lifecycleScope.launch {
            val userLocationAux: Location? = myMap.getUserLocation(this@MainActivity, permisosHandler)

            if (userLocationAux != null) {
                val userLocation = GeoPoint(userLocationAux.latitude, userLocationAux.longitude)
                myMap.goToLocation(userLocation)
                myMap.userLocation = userLocation
            }
        }
    }

    fun addMyMarker(view: View) {
        myMap.addMyMarker()
    }

    private fun handleMapCreation() {
        progressBar.visibility = View.VISIBLE

        myMap.mapView.setTileSource(TileSourceFactory.MAPNIK)
        myMap.mapView.setMultiTouchControls(true)

        myMap.mapView.controller.setZoom(myMap.maxZoom)
        myMap.mapView.controller.setCenter(myMap.startPoint)

        // Quitar el circulo de carga
        myMap.mapView.postDelayed({
            progressBar.visibility = View.GONE
        }, 2000)
    }
}