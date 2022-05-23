package kg.geekteck.youtubeapi40.reposiyories

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kg.geekteck.youtubeapi40.core.network.status.Resource
import kg.geekteck.youtubeapi40.data.remote.RemoteDataSource
import kg.geekteck.youtubeapi40.data.remote.models.Playlist
import kotlinx.coroutines.Dispatchers

class PlaylistRepository(private val dataSource: RemoteDataSource) {

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