package kg.geekteck.youtubeapi40.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kg.geekteck.youtubeapi40.R
import kg.geekteck.youtubeapi40.core.network.status.NetworkStatus
import kg.geekteck.youtubeapi40.core.network.status.Status
import kg.geekteck.youtubeapi40.core.ui.BaseNavFragment
import kg.geekteck.youtubeapi40.core.ui.BaseViewModel
import kg.geekteck.youtubeapi40.core.ui.extensions.showToast
import kg.geekteck.youtubeapi40.core.ui.interfaces.SomethingClicked
import kg.geekteck.youtubeapi40.core.utils.InternetChecker
import kg.geekteck.youtubeapi40.data.remote.models.Item
import kg.geekteck.youtubeapi40.data.remote.models.Video
import kg.geekteck.youtubeapi40.databinding.FragmentPlaylistBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : BaseNavFragment<FragmentPlaylistBinding,
        PlaylistViewModel>(), SomethingClicked {

    private lateinit var args: PlaylistFragmentArgs
    override val viewModel: PlaylistViewModel by viewModel()
    private val connectivityStatus: InternetChecker by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args = PlaylistFragmentArgs.fromBundle(requireArguments())
    }

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentPlaylistBinding {
        return FragmentPlaylistBinding.inflate(inflater)
    }

    override fun checkInternet() {
        super.checkInternet()
        connectivityStatus.observe(requireActivity()) {
            when (it) {
                NetworkStatus.Available -> {
                    try {
                        initViewModel()
                    } catch (e: Exception) {
                    }
                }
                NetworkStatus.Unavailable -> {
                    viewModel.loading.postValue(true)
                }
            }
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        viewModel.loading.observe(this){
            binding.lineLoading?.isVisible = it
            binding.clMain?.isVisible = !it
        }
        binding.rec?.layoutManager = LinearLayoutManager(requireContext())
        args.id?.let { it ->
            viewModel.getPlaylist(it).observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        initDate(it.data?.items)
                        viewModel.loading.postValue(false)
                    }
                    Status.ERROR -> checkInternet()
                    else -> {
                        viewModel.loading.postValue(true)
                    }
                }
            }
        }
    }

    private fun initDate(items: List<Item>?) {
        val videosDuration = arrayListOf<Video>()
        items?.forEach { it ->
            viewModel.getVideo(it.contentDetails.videoId).observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { it1 ->
                            videosDuration.add(Video(it1.items[0].id,
                                it1.items[0].contentDetails.duration))
                        }
                        if (videosDuration.size == items.size) {
                            binding.lineLoadingPlaylist?.visibility = View.GONE
                            binding.rec?.visibility = View.VISIBLE
                            binding.rec?.adapter = PlaylistAdapter(items, requireContext(), videosDuration, this)
                        }
                    }
                    Status.ERROR -> {
                        checkInternet()
                    }
                    Status.LOADING -> {
                        binding.lineLoadingPlaylist?.visibility = View.VISIBLE
                        binding.rec?.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun initView() {
        super.initView()
        try {
            //binding.tvDescription?.movementMethod = LinkMovementMethod.getInstance()
            args.playlistTitle?.let { binding.tvPlaylistTitle?.text = it }
            args.playlistDescription?.let { binding.tvDescription?.text = it }
            args.itemCount?.let {
                binding.tvCountOfVideo?.text = requireContext()
                    .getString(R.string.videos, it)
            }
            args.id?.let { requireContext().showToast(it) }
        } catch (e: Exception) {
        }
    }

    override fun clickOnItem(any: Any) {
        if (any is Item){
            requireContext().showToast("Clicked "+any.contentDetails.videoId)
            navigate(PlaylistFragmentDirections.actionPlaylistFragmentToVideoDetailFragment(args.id!!,
                any.contentDetails.videoId))
        }
    }
}