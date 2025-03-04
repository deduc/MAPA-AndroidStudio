package com.proyecto.mapa

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermisosHandler() {
    lateinit var context: Activity
    lateinit var permissionsLocation: Array<String>
    var PERMISSION_LOCATION_REQUEST_CODE: Int = -1

    constructor(context: Activity, ): this() {
        this.context = context

        permissionsLocation = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        PERMISSION_LOCATION_REQUEST_CODE = PermissionCodes.PERMISSION_LOCATION_REQUEST_CODE
    }

    fun requestPermissionLocation(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity, permissionsLocation, PERMISSION_LOCATION_REQUEST_CODE
        )
    }

    fun hasPermissionLocation(context: Context): Boolean {
        var value = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return value
    }

    fun userActivatedGPSAndLocationPermission(context: Context, permisosHandler: PermisosHandler): Boolean {
        var locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var isGPSEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        var userLocationPermission: Boolean = doHandleLocationPermissions(context as Activity, permisosHandler)

        if (! isGPSEnabled) {
            InfoMessages.showDialogShort(context, InfoMessages.ERROR_GPS_NOT_ACTIVATED)
            return false
        }
        else if (! userLocationPermission) {
            InfoMessages.showDialogShort(context, InfoMessages.ERROR_NOT_LOCATION_PERMISSIONS)
            return false
        }
        else {
            return true
        }
    }

    fun doHandleLocationPermissions(activityContext: Activity, permisosHandler: PermisosHandler): Boolean {
        if (! permisosHandler.hasPermissionLocation(activityContext)) {
            permisosHandler.requestPermissionLocation(activityContext)
            return false
        }
        else {
            return true
        }
    }

}
