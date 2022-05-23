package kg.geekteck.youtubeapi40.di

import kg.geekteck.youtubeapi40.reposiyories.PlaylistRepository
import org.koin.core.module.Module
import org.koin.dsl.module

var repoModules: Module = module {
    single { PlaylistRepository(get()) }
}