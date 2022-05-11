package kg.geekteck.youtubeapi40

import android.view.LayoutInflater
import android.view.View
import kg.geekteck.youtubeapi40.base.BaseActivity
import kg.geekteck.youtubeapi40.base.BaseViewModel
import kg.geekteck.youtubeapi40.databinding.ActivityMainBinding
import kg.geekteck.youtubeapi40.extensions.InternetChecker
import kg.geekteck.youtubeapi40.utils.NetworkStatus

class MainActivity : BaseActivity<BaseViewModel, ActivityMainBinding>(){
    override fun inflateViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun checkInternet() {
        super.checkInternet()
        InternetChecker(this@MainActivity).observe(this) {
            binding.root.getViewById(R.id.inc).visibility = when (it) {
                NetworkStatus.Available -> {
                    View.GONE
                }
                NetworkStatus.Unavailable -> View.VISIBLE
            }
            binding.navHost.visibility = when (it) {
                NetworkStatus.Available -> View.VISIBLE
                NetworkStatus.Unavailable -> View.GONE
            }
        }
    }
    override val viewModel: BaseViewModel
        get() = TODO("Not yet implemented")
}