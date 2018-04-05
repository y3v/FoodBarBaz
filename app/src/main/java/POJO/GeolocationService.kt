package POJO

import POJO.GeolocationService.Companion.GPS_FILTER
import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.os.Looper
import android.util.Log
import java.security.Provider

class GeolocationService : LocationListener, Service() {
    companion object { @JvmStatic val GPS_FILTER :String = "POJO.GPS_SERVICE" }
    lateinit var serviceThread :Thread
    lateinit var locationManager :LocationManager
    var isRunning :Boolean = true

    override fun onBind(intent: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        print("In onStartCommand")
        serviceThread = Thread( Runnable {
            kotlin.run {
                getGPSCoordsCoarse()
                getGPSCoordsFine()
            }
        })

        serviceThread.start()
        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("MissingPermission")
    private fun getGPSCoordsCoarse(){
        val locationManager :LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val criteria = Criteria()
        val provider = locationManager.getBestProvider(criteria,false)
        val location = locationManager.getLastKnownLocation(provider) as Location

        val intent = Intent(GPS_FILTER)
        intent.putExtra("latitude", location.latitude)
        intent.putExtra("longitude", location.longitude)
        intent.putExtra("provider", provider)

        print("lat: " + location.latitude + ", lon: " + location.longitude + ", provider: " + provider)

        sendBroadcast(intent)
    }

    @SuppressLint("MissingPermission")
    private fun getGPSCoordsFine(){
        try {
            Looper.prepare()
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val minTime :Long = 0
            val minDistance :Float = 5.0f

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this)

            Looper.loop()
        }
        catch (e :Exception){
            print(e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        print("ON DESTROY")

        try {
            locationManager.removeUpdates(this)
            isRunning = false
        }
        catch (e :Exception){
            print(e)
        }
    }

    override fun onLocationChanged(location: Location?) {
        val latitude = location!!.latitude
        val longitude = location.longitude

        val intent :Intent = Intent(GPS_FILTER)
        intent.putExtra("latitude", latitude)
        intent.putExtra("longitude", longitude)
        intent.putExtra("provider", location.provider)

        print("lat: $latitude lon: $longitude")
        sendBroadcast(intent)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderEnabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderDisabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
