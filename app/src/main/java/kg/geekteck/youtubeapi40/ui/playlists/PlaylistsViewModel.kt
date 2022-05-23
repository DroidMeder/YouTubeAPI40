package kg.geekteck.youtubeapi40.ui.playlists

import androidx.lifecycle.LiveData
import kg.geekteck.youtubeapi40.core.network.status.Resource
import kg.geekteck.youtubeapi40.core.ui.BaseViewModel
import kg.geekteck.youtubeapi40.data.remote.models.Playlist
import kg.geekteck.youtubeapi40.reposiyories.PlaylistRepository

class PlaylistsViewModel(private val playlistRepository: PlaylistRepository) : BaseViewModel() {

    fun getPlaylists(): LiveData<Resource<Playlist>> {
        return playlistRepository.getPlaylists()
    }
    /*
    //was actual before coroutines
    private val apiService: ApiService by lazy {
        RetrofitClient.create()
    }

    fun getPlaylists(): LiveData<Resource<Playlist>> {
        return playlists()
    }

    private fun playlists(): LiveData<Resource<Playlist>> {
        val data = MutableLiveData<Resource<Playlist>>()
        apiService.getPlaylists(Constants.part, Constants.channelId, API_KEY, Constants.maxResults)
            .enqueue(object :
                Callback<Playlist> {
                override fun onResponse(call: Call<Playlist>, response: Response<Playlist>) {
                    if (response.isSuccessful && response.body() != null) {
                        val res = response.body()
                        data.value = res?.let{
                            Resource.success(it)
                        }
                    } else {
                        data.value = Resource.error(response.message(), null)
                    }
                }

                override fun onFailure(call: Call<Playlist>, t: Throwable) {
                    data.value = Resource.error(t.localizedMessage, null)
                }
            })
        return data
    }*/
}