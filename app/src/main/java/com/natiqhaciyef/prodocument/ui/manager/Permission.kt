package com.natiqhaciyef.prodocument.ui.manager

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE

sealed class Permission(vararg val permissions: String) {
    class CustomPermission(permissions: String): Permission(permissions)
    // Individual permissions
    data object Camera : Permission(CAMERA)

    // Bundled permissions
    data object MandatoryForFeatureOne : Permission(WRITE_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION)

    // Grouped permissions
    data object Location : Permission(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
    data object Storage : Permission(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)


    companion object {
        fun createCustomPermission(permissions: String): CustomPermission {
            return CustomPermission(permissions)
        }

        fun from(permission: String) = when (permission) {
            ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION -> Location
            WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE -> Storage
            CAMERA -> Camera
            else -> CustomPermission(permission)
        }
    }
}