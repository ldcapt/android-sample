package com.example.recyclersample.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.IntRange

class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val type = getConnectionType(context)
        if (type == 0) {
            Toast.makeText(context, "No Internet available", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Currently online", Toast.LENGTH_SHORT).show()
        }
        Log.d("NetworkChangeReceiver", type.toString())
    }

    @IntRange(from = 0, to = 3)
    private fun getConnectionType(context: Context?) : Int {
        var result = 0
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = 2
                    } else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = 1
                    } else if (hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                        result = 3
                    }
                }
            }
        } else {
            cm.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = 2
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = 1
                    } else if (type == ConnectivityManager.TYPE_VPN) {
                        result = 3
                    }
                }
            }
        }
        return  result
    }
}