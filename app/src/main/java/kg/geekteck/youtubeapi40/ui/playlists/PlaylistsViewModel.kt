package kg.geekteck.youtubeapi40.ui.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kg.geekteck.youtubeapi40.BuildConfig.API_KEY
import kg.geekteck.youtubeapi40.base.BaseViewModel
import kg.geekteck.youtubeapi40.models.Playlist
import kg.geekteck.youtubeapi40.objects.Constants
import kg.geekteck.youtubeapi40.remote.ApiService
import kg.geekteck.youtubeapi40.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaylistsViewModel : BaseViewModel() {
    private val apiService: ApiService by lazy {
        RetrofitClient.create()
    }

    fun getPlaylists(): LiveData<Playlist> {
        return playlists()
    }

    private fun playlists(): LiveData<Playlist> {
        val data = MutableLiveData<Playlist>()
        apiService.getPlaylists(Constants.part, Constants.channelId, API_KEY, Constants.maxResults)
            .enqueue(object :
                Callback<Playlist> {
                override fun onResponse(call: Call<Playlist>, response: Response<Playlist>) {
                    if (response.isSuccessful) {
                        data.value = response.body()
                    }
                }

                override fun onFailure(call: Call<Playlist>, t: Throwable) {
                    print(t.stackTrace)
                }
            })
        return data
    }
}