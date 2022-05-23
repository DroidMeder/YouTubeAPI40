package kg.geekteck.youtubeapi40.ui.videodetail

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import kg.geekteck.youtubeapi40.R
import kg.geekteck.youtubeapi40.core.network.status.NetworkStatus
import kg.geekteck.youtubeapi40.core.network.status.Status
import kg.geekteck.youtubeapi40.core.ui.BaseNavFragment
import kg.geekteck.youtubeapi40.core.ui.extensions.chooseTheMostQualityImage
import kg.geekteck.youtubeapi40.core.ui.extensions.glideSetter
import kg.geekteck.youtubeapi40.core.ui.extensions.showToast
import kg.geekteck.youtubeapi40.core.utils.InternetChecker
import kg.geekteck.youtubeapi40.data.remote.models.Item
import kg.geekteck.youtubeapi40.data.remote.models.Playlist
import kg.geekteck.youtubeapi40.databinding.FragmentVideoDetailBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class VideoDetailFragment : BaseNavFragment<FragmentVideoDetailBinding, VideoDetailVewModel>() {

    override val viewModel: VideoDetailVewModel by viewModel()
    private val connectivityStatus: InternetChecker by inject()
    private lateinit var args: VideoDetailFragmentArgs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args = VideoDetailFragmentArgs.fromBundle(requireArguments())
    }

    override fun checkInternet() {
        super.checkInternet()
        requireContext().showToast("OnThird Fragment")
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
            binding.lineLoading.isVisible=it
            binding.clMain.isVisible = !it
        }
        args.id?.let { it ->
            viewModel.getPlaylist(it).observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        initDate(it.data?.items, args.videoId)
                        initSkip(it.data?.items, args.videoId)
                    }
                    Status.ERROR -> checkInternet()
                    else -> {
                        viewModel.loading.postValue(true)
                    }
                }
            }
        }
    }
    companion object{
        var indexVideoInPlaylist = 0
    }

    private fun initSkip(items: List<Item>?, videoId: String?) {
        if (items != null && videoId != null) {
            if (items.size >1){
                for ((index, element)in items.withIndex()){
                    if (element.contentDetails.videoId == videoId){
                        indexVideoInPlaylist = index
                    }
                }
                when(indexVideoInPlaylist){
                    0 ->{
                        binding.ivPrevious.isClickable = false
                        binding.ivNext.isClickable = true
                        binding.ivNext.setOnClickListener {
                            loadVideo(items[indexVideoInPlaylist+1].contentDetails.videoId)
                            initDate(items, items[indexVideoInPlaylist+1].contentDetails.videoId)
                        }
                    }
                    items.size-1 -> {
                        binding.ivPrevious.isClickable = true
                        binding.ivNext.isClickable = false
                        binding.ivPrevious.setOnClickListener {
                            loadVideo(items[indexVideoInPlaylist-1].contentDetails.videoId)
                            initDate(items, items[indexVideoInPlaylist-1].contentDetails.videoId)
                        }
                    }
                    else -> {
                        binding.ivPrevious.isClickable = true
                        binding.ivNext.isClickable = true
                        binding.ivNext.setOnClickListener {
                            loadVideo(items[indexVideoInPlaylist+1].contentDetails.videoId)
                            initDate(items, items[indexVideoInPlaylist+1].contentDetails.videoId)
                        }
                        binding.ivPrevious.setOnClickListener {
                            loadVideo(items[indexVideoInPlaylist-1].contentDetails.videoId)
                            initDate(items, items[indexVideoInPlaylist-1].contentDetails.videoId)
                        }
                    }
                }
            } else {
                binding.ivPrevious.isClickable = false
                binding.ivNext.isClickable = false
            }
        }else{
            requireContext().showToast("There's something wrong!!!")
        }
    }

    private fun initDate(items: List<Item>?, videoId: String?) {
        if (items != null && videoId != null) {
            var indexVideoInPlaylist = 0
            if (items.size >1){
                for ((index, element)in items.withIndex()){
                    if (element.contentDetails.videoId == videoId){
                        indexVideoInPlaylist = index
                    }
                }
                when(indexVideoInPlaylist){
                    0 ->{
                        binding.ivPrevious.isClickable = false
                        binding.ivNext.isClickable = true
                        binding.ivPrevious.setImageResource(R.drawable.ic_baseline_no_skip_previous)
                        binding.ivNext.setImageResource(R.drawable.ic_baseline_skip_next)
                    }
                    items.size-1 -> {
                        binding.ivPrevious.isClickable = true
                        binding.ivNext.isClickable = false
                        binding.ivPrevious.setImageResource(R.drawable.ic_baseline_skip_previous)
                        binding.ivNext.setImageResource(R.drawable.ic_baseline_no_skip_next)
                    }
                    else -> {
                        binding.ivNext.setImageResource(R.drawable.ic_baseline_skip_next)
                        binding.ivPrevious.setImageResource(R.drawable.ic_baseline_skip_previous)
                        binding.ivPrevious.isClickable = true
                        binding.ivNext.isClickable = true
                    }
                }
            } else {
                binding.ivPrevious.isClickable = false
                binding.ivNext.isClickable = false
                binding.ivNext.setImageResource(R.drawable.ic_baseline_no_skip_next)
                binding.ivPrevious.setImageResource(R.drawable.ic_baseline_no_skip_previous)
            }
            loadVideo(videoId)
        }else{
            requireContext().showToast("There's something wrong!!!")
        }
        initSkip(items, videoId)
    }

    private fun loadVideo(videoId: String) {
        viewModel.getVideo(videoId).observe(this){
            when (it.status) {
                Status.SUCCESS -> {
                    intFinalView(it.data)
                    viewModel.loading.postValue(false)
                }
                Status.ERROR -> checkInternet()
                else -> {
                    viewModel.loading.postValue(true)
                }
            }
        }
    }

    private fun intFinalView(it: Playlist?) {
        val uri = it?.let { requireContext().chooseTheMostQualityImage(it.items[0])}
        if (uri != null) {
            requireContext().glideSetter(uri, binding.ivVideo)
        }
        binding.tvVideoTitle.text = it?.let { it.items[0].snippet.title }
        binding.tvDescription.text = it?.let { it.items[0].snippet.description }
    }

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentVideoDetailBinding {
        return FragmentVideoDetailBinding.inflate(inflater)
    }
}