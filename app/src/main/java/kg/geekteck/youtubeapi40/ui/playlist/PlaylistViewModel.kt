package kg.geekteck.youtubeapi40.ui.playlist

import androidx.lifecycle.LiveData
import kg.geekteck.youtubeapi40.base.BaseViewModel
import kg.geekteck.youtubeapi40.models.Playlist
import kg.geekteck.youtubeapi40.reposiyories.PlaylistRepository
import kg.geekteck.youtubeapi40.utils.Resource

class PlaylistViewModel : BaseViewModel() {
    private val playlistRepository = PlaylistRepository()

    fun getPlaylist(playlistId: String): LiveData<Resource<Playlist>> {
        return playlistRepository.getPlaylist(playlistId)
    }

    fun getVideo(id: String): LiveData<Resource<Playlist>> {
        return playlistRepository.getVideo(id)
    }
}