package com.proyecto.mapa

import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.views.overlay.MapEventsOverlay


class MyMap {
    // Madrid
    var startPoint: GeoPoint = GeoPoint(40.4168, -3.7038)
    val maxZoom: Double = 20.0
    lateinit var mapView: MapView
    lateinit var userLocation: GeoPoint
    private var userClickedAddMarkerButton = false

    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("MissingPermission")
    suspend fun getUserLocation(context: Context, permisosHandler: PermisosHandler): Location? {
        if (! permisosHandler.userActivatedGPSAndLocationPermission(context, permisosHandler)) {
            return  null
        }

        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        return suspendCancellableCoroutine { cont: CancellableContinuation<Location?> ->
            fusedLocationProviderClient.lastLocation.apply {
                if (isComplete) {
                    if (isSuccessful) {
                        cont.resume(result){}
                    }
                    else {
                        cont.resume(null){}
                    }
                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener {
                    cont.resume(it){}
                }
                addOnFailureListener {
                    cont.resume(null){}
                }
                addOnCanceledListener {
                    cont.resume(null){}
                }
            }
        }
    }

    private fun addMarkerPoint(location: GeoPoint, texto: String? = "Punto de Interés") {
        val marker = Marker(this.mapView).apply {
            position = location
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            title = texto
        }

        this.mapView.overlays.add(marker)
    }

    fun addMyMarker() {
        userClickedAddMarkerButton = true

        val mapEventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                if (userClickedAddMarkerButton) { // Solo permite añadir un marcador si está activado
                    p?.let {
                        this@MyMap.addMarker(it)
                        userClickedAddMarkerButton = false // Desactivar después de un solo toque
                    }
                }
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }
        }

        val overlayEvents = MapEventsOverlay(mapEventsReceiver)
        this.mapView.overlays.add(overlayEvents)
    }

    fun goToLocation(geoPoint: GeoPoint) {
        this.addMarkerPoint(geoPoint, "Tu ubicación")

        // DEPRECATED - Refrescar el mapa
        this.mapView.invalidate()

        this.mapView.controller.setZoom(this.maxZoom)
        this.mapView.controller.setCenter(geoPoint)
    }

    private fun addMarker(position: GeoPoint) {
        val marker = Marker(this.mapView)
        marker.position = position
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Marcador"
        marker.snippet = "Lat: ${position.latitude}, Lon: ${position.longitude}"

        this.mapView.overlays.add(marker)
        this.mapView.invalidate()
    }
}
