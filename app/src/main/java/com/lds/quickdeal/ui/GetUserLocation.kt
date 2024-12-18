package com.lds.quickdeal.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


///**
// * Composable function to request location permissions and handle different scenarios.
// *
// * @param onPermissionGranted Callback to be executed when all requested permissions are granted.
// * @param onPermissionDenied Callback to be executed when any requested permission is denied.
// * @param onPermissionsRevoked Callback to be executed when previously granted permissions are revoked.
// */
//
//@OptIn(ExperimentalPermissionsApi::class)
//@Composable
//fun RequestLocationPermissionUsingRememberMultiplePermissionsState(
//    onPermissionGranted: () -> Unit,
//    onPermissionDenied: () -> Unit,
//    onPermissionsRevoked: () -> Unit
//) {
//    // Initialize the state for managing multiple location permissions.
//    //It takes a list of permissions as input and a lambda function as a callback.
//    val permissionState = rememberMultiplePermissionsState(
//        listOf(
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//        )
//    ){ permissionsMap ->
//        val arePermissionsGranted = permissionsMap.values.reduce { acc, next ->
//            acc && next
//        }
//
//        if (arePermissionsGranted) { onPermissionGranted.invoke() } else { onPermissionDenied.invoke() }
//    }
//
//    // Use LaunchedEffect to handle permissions logic when the composition is launched.
//    LaunchedEffect(key1 = permissionState) {
//        // Check if all previously granted permissions are revoked.
//        val allPermissionsRevoked =
//            permissionState.permissions.size == permissionState.revokedPermissions.size
//
//        // Filter permissions that need to be requested.
//        val permissionsToRequest = permissionState.permissions.filter {
//            !it.status.isGranted
//        }
//
//        // If there are permissions to request, launch the permission request.
//        if (permissionsToRequest.isNotEmpty()) permissionState.launchMultiplePermissionRequest()
//
//        // Execute callbacks based on permission status.
//        if (allPermissionsRevoked) {
//            onPermissionsRevoked()
//        } else {
//            if (permissionState.allPermissionsGranted) {
//                onPermissionGranted()
//            } else {
//                onPermissionDenied()
//            }
//        }
//    }
//}