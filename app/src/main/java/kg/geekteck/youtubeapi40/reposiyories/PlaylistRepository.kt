package kg.geekteck.youtubeapi40.reposiyories

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kg.geekteck.youtubeapi40.models.Playlist
import kg.geekteck.youtubeapi40.remote.RemoteDataSource
import kg.geekteck.youtubeapi40.utils.Resource
import kotlinx.coroutines.Dispatchers

class PlaylistRepository {
    private val dataSource = RemoteDataSource()

    fun getPlaylists(): LiveData<Resource<Playlist>> = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        val response = dataSource.getPlaylists()
        emit(response)
    }

    fun getPlaylist(playlistId: String): LiveData<Resource<Playlist>> = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        val response = dataSource.getPlaylist(playlistId)
        emit(response)
    }

    fun getVideo(id: String): LiveData<Resource<Playlist>> = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        val response = dataSource.getVideo(id)
        emit(response)
    }


}