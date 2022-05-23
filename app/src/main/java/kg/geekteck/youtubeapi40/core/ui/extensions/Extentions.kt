package kg.geekteck.youtubeapi40.core.ui.extensions

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import kg.geekteck.youtubeapi40.data.remote.models.Item
import kg.geekteck.youtubeapi40.data.remote.models.Video

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

fun replacer(video: Video): String {
    val st = video.duration.replace("S", "")
        .replace("PT", "").replace("M", ":")
    return if (st.contains("H")){
        if (st.contains("DT")){
            st.replace("DT", ":")
                .replace("H", ":")
        } else {
            st.replace("H", ":")
        }
    } else {
        st
    }
}

fun Context.showToast(string: String?){
    Toast.makeText(this, string, Toast.LENGTH_LONG).show()
}

fun Context.glideSetter(uri: String, view: ImageView){
    Glide.with(this).load(uri).centerCrop().into(view)
}

/*class InternetChecker(context: Context) : LiveData<NetworkStatus>(){
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
            internetList.clear()
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
}*/
