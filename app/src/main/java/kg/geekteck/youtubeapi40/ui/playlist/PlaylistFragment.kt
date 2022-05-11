package kg.geekteck.youtubeapi40.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import kg.geekteck.youtubeapi40.base.BaseNavFragment
import kg.geekteck.youtubeapi40.base.BaseViewModel
import kg.geekteck.youtubeapi40.databinding.FragmentPlaylistBinding
import kg.geekteck.youtubeapi40.extensions.InternetChecker
import kg.geekteck.youtubeapi40.extensions.showToast
import kg.geekteck.youtubeapi40.utils.NetworkStatus

class PlaylistFragment : BaseNavFragment<FragmentPlaylistBinding, BaseViewModel>() {
    private lateinit var args: PlaylistFragmentArgs
    override val viewModel: BaseViewModel
        get() = TODO("Not yet implemented")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args = PlaylistFragmentArgs.fromBundle(requireArguments())
    }

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentPlaylistBinding {
        return FragmentPlaylistBinding.inflate(inflater)
    }

    override fun checkInternet() {
        super.checkInternet()
        InternetChecker(requireContext()).observe(requireActivity()) {
            when (it) {
                NetworkStatus.Available -> initView()
                NetworkStatus.Unavailable -> println("---1 No Internet")
            }
        }
    }

    override fun initView() {
        super.initView()
        try {
            args.id?.let { requireContext().showToast(it) }
        } catch (e: Exception) {
        }
    }
}