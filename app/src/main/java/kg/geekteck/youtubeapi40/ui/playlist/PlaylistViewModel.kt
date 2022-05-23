package kg.geekteck.youtubeapi40.ui.playlist

import androidx.lifecycle.LiveData
import kg.geekteck.youtubeapi40.core.network.status.Resource
import kg.geekteck.youtubeapi40.core.ui.BaseViewModel
import kg.geekteck.youtubeapi40.data.remote.models.Playlist
import kg.geekteck.youtubeapi40.reposiyories.PlaylistRepository

class PlaylistViewModel(private val playlistRepository: PlaylistRepository) : BaseViewModel() {

    fun getPlaylist(playlistId: String): LiveData<Resource<Playlist>> {
        return playlistRepository.getPlaylist(playlistId)
    }

    fun getVideo(id: String): LiveData<Resource<Playlist>> {
        return playlistRepository.getVideo(id)
    }
}