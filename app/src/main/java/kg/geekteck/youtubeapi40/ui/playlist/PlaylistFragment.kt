package kg.geekteck.youtubeapi40.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kg.geekteck.youtubeapi40.R
import kg.geekteck.youtubeapi40.base.BaseNavFragment
import kg.geekteck.youtubeapi40.base.BaseViewModel
import kg.geekteck.youtubeapi40.databinding.FragmentPlaylistBinding
import kg.geekteck.youtubeapi40.extensions.InternetChecker
import kg.geekteck.youtubeapi40.extensions.showToast
import kg.geekteck.youtubeapi40.interfaces.SomethingClicked
import kg.geekteck.youtubeapi40.models.Item
import kg.geekteck.youtubeapi40.models.Video
import kg.geekteck.youtubeapi40.utils.NetworkStatus
import kg.geekteck.youtubeapi40.utils.Status

class PlaylistFragment : BaseNavFragment<FragmentPlaylistBinding,
        BaseViewModel>(), SomethingClicked {
    private lateinit var args: PlaylistFragmentArgs
    override val viewModel: PlaylistViewModel by lazy {
        ViewModelProvider(requireActivity())[PlaylistViewModel::class.java]
    }

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
                NetworkStatus.Available -> {
                    try {
                        initViewModel()
                    } catch (e: Exception) {
                    }
                }
                NetworkStatus.Unavailable -> {
                    binding.lineLoading?.visibility = View.VISIBLE
                    binding.clMain?.visibility = View.GONE
                }
            }
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        binding.rec?.layoutManager = LinearLayoutManager(requireContext())
        args.id?.let { it ->
            viewModel.getPlaylist(it).observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        initDate(it.data?.items)
                        binding.lineLoading?.visibility = View.GONE
                        binding.clMain?.visibility = View.VISIBLE
                    }
                    Status.ERROR -> checkInternet()
                    else -> {
                        binding.lineLoading?.visibility = View.VISIBLE
                        binding.clMain?.visibility = View.GONE
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
                            binding.rec?.adapter = PlaylistAdapter(items, requireContext(), videosDuration)
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
        TODO("Not yet implemented")
    }
}