package com.abdulmughni.personal.openweatherandroid.core.utils

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class OnMapAndViewReadyListener(
        private val mapFragment: SupportMapFragment,
        private val toBeNotified: OnGlobalLayoutAndMapReadyListener
) : OnGlobalLayoutListener,
        OnMapReadyCallback {

    private val mapView: View? = mapFragment.view

    private var isViewReady = false
    private var isMapReady = false
    private var map: GoogleMap? = null

    interface OnGlobalLayoutAndMapReadyListener {
        fun onMapReady(googleMap: GoogleMap?)
    }

    init {
        registerListeners()
    }

    private fun registerListeners() {
        if (mapView?.width != 0 && mapView?.height != 0) {
            isViewReady = true
        } else {
            mapView.viewTreeObserver.addOnGlobalLayoutListener(this)
        }
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return
        isMapReady = true
        fireCallbackIfReady()
    }

    @Suppress("DEPRECATION")
    @SuppressLint("NewApi")
    override fun onGlobalLayout() {
        mapView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
        isViewReady = true
        fireCallbackIfReady()
    }

    private fun fireCallbackIfReady() {
        if (isViewReady && isMapReady) {
            toBeNotified.onMapReady(map)
        }
    }
}
