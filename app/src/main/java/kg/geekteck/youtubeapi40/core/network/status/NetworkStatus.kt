package kg.geekteck.youtubeapi40.core.network.status

sealed class NetworkStatus {
    object Available : NetworkStatus()
    object Unavailable : NetworkStatus()
}
