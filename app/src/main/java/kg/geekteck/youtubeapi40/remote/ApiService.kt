package kg.geekteck.youtubeapi40.remote

import kg.geekteck.youtubeapi40.models.Playlist
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("playlists")
    suspend fun getPlaylists(
        @Query("part") part: String,
        @Query("channelId") channelId: String,
        @Query("key") apiKey: String,
        @Query("maxResults") maxResults: Int
    ): Response<Playlist>

    @GET("playlistItems")
    suspend fun getPlaylist(
        @Query("part") part: String,
        @Query("playlistId") playlistId: String,
        @Query("key") apiKey: String,
        @Query("maxResults") maxResults: Int
    ): Response<Playlist>

    @GET("videos")
    suspend fun getVideo(
        @Query("part") part: String,
        @Query("id") id: String,
        @Query("key") apiKey: String,
        @Query("maxResults") maxResults: Int
    ): Response<Playlist>
}