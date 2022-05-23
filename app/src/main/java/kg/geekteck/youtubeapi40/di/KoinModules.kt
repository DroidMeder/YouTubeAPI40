package kg.geekteck.youtubeapi40.di

import kg.geekteck.youtubeapi40.core.network.networkModule
import kg.geekteck.youtubeapi40.core.utils.connectivityStatus

var koinModules = listOf(
    repoModules,
    viewModels,
    networkModule,
    connectivityStatus
)