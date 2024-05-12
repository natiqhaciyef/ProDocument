package com.natiqhaciyef.prodocument.ui.manager

import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

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
                .setTitle("Permission")
                .setMessage(rationale ?: "Permission need. Do you want give permission ?")
                .setCancelable(true)
                .setPositiveButton("Yes") { _, _ ->
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
}