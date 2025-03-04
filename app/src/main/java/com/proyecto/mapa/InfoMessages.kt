package com.proyecto.mapa

import android.content.Context
import android.widget.Toast

object InfoMessages {
    const val INFO_HAS_LOCATION_PERMISSIONS = "Info: Hay permisos de localización"
    const val INFO_WE_NEED_LOCATION_PERMISSIONS = "Info: Esta aplicación necesita acceder a tu ubicación."

    const val ERROR_GPS_NOT_ACTIVATED = "ERROR: El GPS no está activado."
    const val ERROR_NOT_LOCATION_PERMISSIONS = "ERROR: No hay permisos de ubicación"

    fun showDialogLong(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showDialogShort(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}