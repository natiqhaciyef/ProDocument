package com.natiqhaciyef.prodocument.ui.manager

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.natiqhaciyef.common.helpers.capitalizeFirstLetter
import com.natiqhaciyef.prodocument.ui.manager.PermissionManager.Permission.Companion.PERMISSION
import com.natiqhaciyef.prodocument.ui.manager.PermissionManager.Permission.Companion.PERMISSION_REQUIREMENT
import com.natiqhaciyef.prodocument.ui.manager.PermissionManager.Permission.Companion.YES

class PermissionManager private constructor(
    private val fragment: Fragment,
    private val requiredPermissions: MutableList<Permission> = mutableListOf(),
    private var rationale: String? = null,
    private var callback: (Boolean) -> Unit = {},
    private var detailedCallback: (Map<Permission, Boolean>) -> Unit = {},
) {
    data class Builder(
        private val fragment: Fragment,
        private val isMultiple: Boolean,
        private val requiredPermissions: MutableList<Permission> = mutableListOf(),
        private var rationale: String? = null,
        private var callback: (Boolean) -> Unit = {},
        private var detailedCallback: (Map<Permission, Boolean>) -> Unit = {},
        private var permissionListCheck: ActivityResultLauncher<Array<String>>? = null,
        private var permissionCheck: ActivityResultLauncher<String>? = null
    ) {

        fun addPermissionLauncher(launcher: ActivityResultLauncher<*>) = apply {
            if (isMultiple)
                this.permissionListCheck = launcher as ActivityResultLauncher<Array<String>>
            else
                this.permissionCheck = launcher as ActivityResultLauncher<String>
        }

        fun rationale(description: String) = apply {
            this.rationale = description
        }

        fun request(vararg permission: Permission) = apply {
            this.requiredPermissions.addAll(permission)
        }

        fun checkPermission(callback: (Boolean) -> Unit) = apply {
            this.callback = callback
            handlePermissionRequest()
        }

        private fun handlePermissionRequest() = apply {
            this.fragment.let { fragment ->
                when {
                    areAllPermissionsGranted(fragment) -> sendPositiveResult()
                    shouldShowPermissionRationale(fragment) -> displayRationale(fragment)
                    else -> requestPermissions()
                }
            }
        }

        private fun displayRationale(fragment: Fragment) {
            AlertDialog.Builder(fragment.requireContext())
                .setTitle(PERMISSION.capitalizeFirstLetter())
                .setMessage(rationale ?: PERMISSION_REQUIREMENT)
                .setCancelable(true)
                .setPositiveButton(YES.capitalizeFirstLetter()) { _, _ ->
                    requestPermissions()
                }
                .show()
        }

        private fun sendPositiveResult() = apply {
            if (isMultiple)
                sendResultListAndCleanUp(getPermissionList().associateWith { true })
            else
                sendResultAndCleanUp(getPermission(), true)
        }

        fun checkDetailedPermission(callback: (Map<Permission, Boolean>) -> Unit) = apply {
            this.detailedCallback = callback
            handlePermissionRequest()
        }

        private fun requestPermissions() {
            if (isMultiple)
                permissionListCheck?.launch(getPermissionList())
            else
                permissionCheck?.launch(getPermission())
        }

        private fun sendResultListAndCleanUp(grantResults: Map<String, Boolean>) {
            callback(grantResults.all { it.value })
            detailedCallback(grantResults.mapKeys { Permission.from(it.key) })
            cleanUp()
        }

        private fun sendResultAndCleanUp(key: String, isGranted: Boolean) {
            callback(isGranted)
            detailedCallback(mapOf(Permission.from(key) to isGranted))
            cleanUp()
        }


        private fun cleanUp() {
            requiredPermissions.clear()
            rationale = null
            callback = {}
            detailedCallback = {}
        }

        private fun areAllPermissionsGranted(fragment: Fragment) =
            requiredPermissions.all { it.isGranted(fragment) }

        private fun shouldShowPermissionRationale(fragment: Fragment) =
            requiredPermissions.any { it.requiresRationale(fragment) }

        private fun getPermissionList() =
            requiredPermissions.flatMap { it.permissions.toList() }.toTypedArray()

        private fun getPermission() =
            requiredPermissions.first().permissions.first()

        private fun Permission.isGranted(fragment: Fragment) =
            permissions.all { hasPermission(fragment, it) }

        private fun Permission.requiresRationale(fragment: Fragment) =
            permissions.any { fragment.shouldShowRequestPermissionRationale(it) }

        private fun hasPermission(fragment: Fragment, permission: String) =
            ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED

        fun build() = PermissionManager(
            this.fragment,
            this.requiredPermissions,
            this.rationale,
            this.callback,
            this.detailedCallback
        )
    }

    sealed class Permission(vararg val permissions: String) {
        class CustomPermission(permissions: String): Permission(permissions)
        // Individual permissions
        data object Camera : Permission(Manifest.permission.CAMERA)

        // Bundled permissions
        data object MandatoryForFeatureOne : Permission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        // Grouped permissions
        data object Location : Permission(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        data object Storage : Permission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )


        companion object {
            const val PERMISSION_REQUIREMENT = "Permission need. Do you want give permission ?"
            const val PERMISSION_REQUEST = "Permission needed for Gallery"
            const val YES = "yes"
            const val PERMISSION = "Permission"

            fun createCustomPermission(permissions: String): CustomPermission {
                return CustomPermission(permissions)
            }

            fun from(permission: String) = when (permission) {
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION -> Location
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE -> Storage
                Manifest.permission.CAMERA -> Camera
                else -> CustomPermission(permission)
            }
        }
    }
}