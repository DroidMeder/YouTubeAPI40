package kg.geekteck.youtubeapi40.extensions

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.bumptech.glide.Glide
import kg.geekteck.youtubeapi40.models.Item
import kg.geekteck.youtubeapi40.objects.InternetAvailability
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Context as Context

fun Context.chooseTheMostQualityImage(i: Item): String {
    return try {
        i.snippet.thumbnails.maxres.url
    } catch (e: Exception) {
        return try {
            i.snippet.thumbnails.standard.url
        } catch (e: Exception) {
            return try {
                i.snippet.thumbnails.high.url
            } catch (e: Exception) {
                try {
                    i.snippet.thumbnails.medium.url
                } catch (e: Exception) {
                    try {
                        i.snippet.thumbnails.default.url
                    } catch (e: Exception) {
                        showToast("There are no images is found")
                        ""
                    }
                }
            }
        }
    }
}

fun Context.showToast(string: String){
    Toast.makeText(this, string, Toast.LENGTH_LONG).show()
}

fun Context.glideSetter(uri: String, view: ImageView){
    Glide.with(this).load(uri).centerCrop().into(view)
}

sealed class NetworkStatus {
    object Available : NetworkStatus()
    object Unavailable : NetworkStatus()
}

class InternetChecker(context: Context) : LiveData<NetworkStatus>(){
    val internetList: ArrayList<Network> = ArrayList()
    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback
    val connectivityManager: ConnectivityManager = context
        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun onActive() {
        super.onActive()
        connectivityManagerCallback = internetCallBack()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, connectivityManagerCallback)
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
    }

    private fun internetCallBack() = object: ConnectivityManager.NetworkCallback(){
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            val networkCapability = connectivityManager.getNetworkCapabilities(network)
            val hasConnection = networkCapability?.hasCapability(NetworkCapabilities
                .NET_CAPABILITY_INTERNET)?:false
            if (hasConnection){
                determine(network)
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            internetList.remove(network)
            announceStatus()
        }

        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)){
                determine(network)
            } else {
                internetList.remove(network)
            }
            announceStatus()
        }
    }

    private fun determine(network: Network) {
        CoroutineScope(Dispatchers.IO).launch {
            if (InternetAvailability.check()){
                withContext(Dispatchers.Main){
                    internetList.add(network)
                    announceStatus()
                }
            }
        }
    }

    private fun announceStatus() {
        if (internetList.isNotEmpty()){
            postValue(NetworkStatus.Available)
        } else {
            postValue(NetworkStatus.Unavailable)
        }
    }
}
