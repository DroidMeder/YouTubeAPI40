package kg.geekteck.youtubeapi40.ui.playlists

import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import kg.geekteck.youtubeapi40.core.network.status.NetworkStatus
import kg.geekteck.youtubeapi40.core.network.status.Status
import kg.geekteck.youtubeapi40.core.ui.BaseNavFragment
import kg.geekteck.youtubeapi40.R
import kg.geekteck.youtubeapi40.core.ui.interfaces.SomethingClicked
import kg.geekteck.youtubeapi40.core.utils.InternetChecker
import kg.geekteck.youtubeapi40.data.remote.models.Item
import kg.geekteck.youtubeapi40.databinding.FragmentPlaylistsBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistsFragment : BaseNavFragment<FragmentPlaylistsBinding, PlaylistsViewModel>(),
    SomethingClicked {

    override val viewModel: PlaylistsViewModel by viewModel()
    private val connectivityStatus: InternetChecker by inject()

    override fun checkInternet() {
        super.checkInternet()
        connectivityStatus.observe(requireActivity()) {
            when (it) {
                NetworkStatus.Available -> initViewModel()
                NetworkStatus.Unavailable -> {
                    viewModel.loading.postValue(true)
                    /*binding.lineLoading.visibility = View.VISIBLE
                    binding.rec.visibility = View.GONE*/
                }
            }
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        binding.rec.layoutManager =LinearLayoutManager(requireContext())
        viewModel.loading.observe(this){
            binding.lineLoading.isVisible = it
            binding.rec.isVisible = !it
        }
        viewModel.getPlaylists().observe(this) {
            when(it.status){
                Status.SUCCESS -> {
                    initDate(it.data?.items)
                    viewModel.loading.postValue(false)
                   /* binding.lineLoading.visibility = View.GONE
                    binding.rec.visibility = View.VISIBLE*/
                }
                Status.ERROR -> checkInternet()
                Status.LOADING -> {
                    viewModel.loading.postValue(true)
                }
            }
        }
    }

    private fun initDate(items: List<Item>?) {
        binding.rec.adapter = items?.let { PlaylistsAdapter(it, this, requireContext()) }
    }

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentPlaylistsBinding {
        return FragmentPlaylistsBinding.inflate(inflater)
    }

    override fun clickOnItem(any: Any) {
        if (any is Item) navigate(PlaylistsFragmentDirections
            .actionPlaylistsFragmentToPlaylistFragment(any.id, any.snippet.title,
                any.snippet.description, any.contentDetails.itemCount.toString()))/*navigate(PlaylistsFragmentDirections
            .actionPlaylistsFragmentToPlaylistFragment(any.id, any.snippet.title,
                any.snippet.description, any.contentDetails.itemCount.toString()))*/
    }
}