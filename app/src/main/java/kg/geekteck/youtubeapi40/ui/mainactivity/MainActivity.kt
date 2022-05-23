package kg.geekteck.youtubeapi40.ui.mainactivity

import android.view.LayoutInflater
import androidx.core.view.isVisible
import kg.geekteck.youtubeapi40.R
import kg.geekteck.youtubeapi40.core.network.status.NetworkStatus
import kg.geekteck.youtubeapi40.core.ui.BaseActivity
import kg.geekteck.youtubeapi40.core.utils.InternetChecker
import kg.geekteck.youtubeapi40.databinding.ActivityMainBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(){

    private val connectivityStatus: InternetChecker by inject()
    override val viewModel: MainViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun checkInternet() {
        super.checkInternet()
        connectivityStatus.observe(this) {
            when(it){
                NetworkStatus.Available -> viewModel.loading.postValue(false)
                NetworkStatus.Unavailable -> viewModel.loading.postValue(true)
            }
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        viewModel.loading.observe(this){
            binding.root.getViewById(R.id.inc).isVisible = it
            binding.navHost.isVisible=!it
        }
    }
}