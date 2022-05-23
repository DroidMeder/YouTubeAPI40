package kg.geekteck.youtubeapi40.core.network

import kg.geekteck.youtubeapi40.core.network.status.Resource
import retrofit2.Response
import java.io.IOException

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T>{
        try {
            val response = call()
            if (response.isSuccessful){
                val body = response.body()
                if (body != null || response.code() in 200..299) {
                    return Resource.success(body!!)
                }
            } else return Resource.error(response.message(), response.body(), response.code())
        } catch (e : IOException){
            return Resource.error(e.message ?: e.toString(), null, 429)
        }

        return Resource.error(null, null, 429)
    }
}