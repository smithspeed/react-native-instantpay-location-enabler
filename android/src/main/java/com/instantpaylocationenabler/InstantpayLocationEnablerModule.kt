package com.instantpaylocationenabler

import android.util.Log
import com.facebook.react.bridge.*
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.IntentSender
import org.json.JSONObject
import org.json.JSONTokener

class InstantpayLocationEnablerModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

        val SUCCESS:String = "Success"
	    val FAILED:String = "Failed"
        lateinit var DATA: String
        private var responsePromise: Promise? = null
        private val REQUEST_TO_TURN_ON_LOCATION = 29

        companion object {
            const val NAME = "InstantpayLocationEnabler"
        }

        override fun getName(): String {
            return NAME
        }

        private val locationPermissionActivityListener = object : BaseActivityEventListener() {
            override fun onActivityResult(
                activity: Activity?,
                requestCode: Int,
                resultCode: Int,
                data: Intent?
            ) {
                super.onActivityResult(activity, requestCode, resultCode, data)

                if (requestCode == REQUEST_TO_TURN_ON_LOCATION) {

                    when(resultCode){
                        RESULT_OK -> {
                            val result = JSONObject();
                            result.put("status", "ENABLE")
                            return resolve("Location is Enable", SUCCESS, result.toString())
                        }
                        RESULT_CANCELED -> {
                            val result = JSONObject();
                            result.put("status", "CANCELED")
                            return resolve("Location is Disable", SUCCESS, result.toString())
                        }
                    }

                }
            }
        }

        init {
            reactContext.addActivityEventListener(locationPermissionActivityListener)
        }

        //For Promise Resolver
        private fun resolve(message:String,status:String = FAILED, data:String = "", actCode:String = "" ) {
            if(responsePromise == null){
                return;
            }

            val output:WritableMap = Arguments.createMap()

            output.putString("status",status)
            output.putString("message",message)
            output.putString("data",data)
            output.putString("actCode",actCode)

            responsePromise!!.resolve(output)
            responsePromise = null
	    }

        //For Log
        private fun logPrint(value: String?) {
            if (value == null) {
                return
            }
            Log.i("IpayLocationEnablerLog*", value)
        }

        private fun getCurrentLocationSetting(options: String?) {

            val defaultOptions = mutableMapOf<String,Boolean>(
                "askDailogPermission" to false,
                "setPermission" to false
            )

            if(options!=null){

                val items = JSONTokener(options).nextValue() as JSONObject

                if(items.has("askDailogPermission") && items.get("askDailogPermission") as Boolean){
                    defaultOptions["askDailogPermission"] = items.get("askDailogPermission") as Boolean
                }
            }

            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY,1000)
                .build()

            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

            val client = LocationServices.getSettingsClient(reactApplicationContext)

            val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

            task.addOnSuccessListener {
                val result = JSONObject();
                result.put("status", "ENABLE")
                return@addOnSuccessListener resolve("Location is Enable", SUCCESS, result.toString())
            }

            task.addOnFailureListener{ exception ->
                if (exception is ResolvableApiException){
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().

                        if(defaultOptions["askDailogPermission"] as Boolean){
                            exception.startResolutionForResult(currentActivity!!,REQUEST_TO_TURN_ON_LOCATION)
                        }
                        else{
                            val result = JSONObject();
                            result.put("status", "DISABLE")
                            return@addOnFailureListener resolve("Location is Disable", SUCCESS, result.toString())
                        }

                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error.
                        //logPrint("requestDeviceResolutionLocationSettings.addOnFailureListener > catch error > " +sendEx.localizedMessage)
                        return@addOnFailureListener resolve(sendEx.localizedMessage + " #LNCFL");
                    }
                }
            }
        }

        // React method

        @ReactMethod
        fun checkLocation(options: String? = null, prm: Promise){
            try {
                responsePromise = prm

                getCurrentLocationSetting(options);
            } catch (e: Exception) {
                resolve(e.message.toString() + " #BMBS");
            }
        }

}
