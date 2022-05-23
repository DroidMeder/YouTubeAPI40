package kg.geekteck.youtubeapi40.di

import kg.geekteck.youtubeapi40.ui.mainactivity.MainViewModel
import kg.geekteck.youtubeapi40.ui.playlist.PlaylistViewModel
import kg.geekteck.youtubeapi40.ui.playlists.PlaylistsViewModel
import kg.geekteck.youtubeapi40.ui.videodetail.VideoDetailVewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModels : Module = module {
    viewModel { MainViewModel() }
    viewModel { PlaylistsViewModel(playlistRepository = get()) }
    viewModel { PlaylistViewModel(playlistRepository = get()) }
    viewModel { VideoDetailVewModel(playlistRepository = get()) }
}