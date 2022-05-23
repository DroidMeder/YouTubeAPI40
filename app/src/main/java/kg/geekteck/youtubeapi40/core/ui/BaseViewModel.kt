package kg.geekteck.youtubeapi40.core.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel(){
    val loading = MutableLiveData<Boolean>()
}