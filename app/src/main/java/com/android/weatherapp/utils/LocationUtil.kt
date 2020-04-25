package com.android.weatherapp.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LocationUtil (
    private val context: Context,
    private val listener: IListener
): LocationListener {
    private val locationMgr: LocationManager =  context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var location: Location? = null
    private var shouldFindCity : Boolean = false
    set(value) {
        field = value
        if(value)
            findCurrentCity()
    }
    private val executor: ExecutorService =  Executors.newSingleThreadExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())

    fun findCurrentCity(){
        if(!shouldFindCity) shouldFindCity = true
        if(location == null) {
            initLocation()
            return
        }
        executor.submit {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addressList = geocoder.getFromLocation(
                    location!!.latitude,
                    location!!.longitude, 1)
                if(addressList == null || addressList.size < 1) return@submit
                val address = addressList[0]
                if(TextUtils.isEmpty(address.locality)) return@submit
                mainHandler.post{listener.onCityFound(address.locality)}
            }catch (ex: Exception){
                ex.printStackTrace()
            }finally {
                shouldFindCity = false
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun initLocation(){
        if(!isPermissionGranted()){
            if(context is Activity)
                ActivityCompat.requestPermissions(context,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE )
            listener.onRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            return
        }
        when {
            locationMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> {
                locationMgr.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                location = locationMgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }
            locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER ) -> {
                locationMgr.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                location = locationMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }
            else -> {
                listener.onError("Please turn on your location")
            }
        }
        shouldFindCity()
    }

    private fun isPermissionGranted(): Boolean =
        Build.VERSION.SDK_INT >= 23 &&
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission( context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    fun onPermissionGranted(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ){
        if(requestCode == REQUEST_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                findCurrentCity()
        }
    }

    private fun shouldFindCity(){
        if(location != null && shouldFindCity)
            findCurrentCity()
    }

    override fun onLocationChanged(location: Location?) {
        this.location = location
        shouldFindCity()
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String?) {}

    override fun onProviderDisabled(provider: String?) {}

    companion object{
        const val REQUEST_CODE: Int = 10
        private const val MIN_TIME_BW_UPDATES: Long = 1000 * 60 * 2
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES : Float = 20000.0F
    }

    interface IListener {
        fun onRequestPermission(vararg permission: String)
        fun onError(msg: String)
        fun onCityFound(city: String)
    }
}