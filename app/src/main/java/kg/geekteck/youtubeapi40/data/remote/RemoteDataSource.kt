package kg.geekteck.youtubeapi40.data.remote

import kg.geekteck.youtubeapi40.BuildConfig
import kg.geekteck.youtubeapi40.core.network.BaseDataSource
import kg.geekteck.youtubeapi40.core.utils.Constants

class RemoteDataSource(private val apiService: ApiService) : BaseDataSource() {

    suspend fun getPlaylists() = getResult {
        apiService.getPlaylists(Constants.part, Constants.channelId, BuildConfig.API_KEY, Constants.maxResults)
    }

    suspend fun getPlaylist(playlistId: String) = getResult {
        apiService.getPlaylist(Constants.part, playlistId, BuildConfig.API_KEY, Constants.maxResults)
    }

    suspend fun getVideo(id: String) = getResult {
        apiService.getVideo(Constants.partForVideo, id, BuildConfig.API_KEY, 1)
    }
}