package kg.geekteck.youtubeapi40.ui.playlists

import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kg.geekteck.youtubeapi40.base.BaseNavFragment
import kg.geekteck.youtubeapi40.base.BaseViewModel
import kg.geekteck.youtubeapi40.databinding.FragmentPlaylistsBinding
import kg.geekteck.youtubeapi40.extensions.InternetChecker
import kg.geekteck.youtubeapi40.interfaces.SomethingClicked
import kg.geekteck.youtubeapi40.models.Item
import kg.geekteck.youtubeapi40.utils.NetworkStatus
import kg.geekteck.youtubeapi40.utils.Status


class PlaylistsFragment : BaseNavFragment<FragmentPlaylistsBinding, BaseViewModel>(),
    SomethingClicked {

    override val viewModel: PlaylistsViewModel by lazy {
        ViewModelProvider(requireActivity())[PlaylistsViewModel::class.java]
    }

    override fun checkInternet() {
        super.checkInternet()
        InternetChecker(requireContext()).observe(requireActivity()) {
            when (it) {
                NetworkStatus.Available -> initViewModel()
                NetworkStatus.Unavailable -> {
                    binding.lineLoading.visibility = View.VISIBLE
                    binding.rec.visibility = View.GONE
                }
            }
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        binding.rec.layoutManager =LinearLayoutManager(requireContext())
        viewModel.getPlaylists().observe(this) {
            when(it.status){
                Status.SUCCESS -> {
                    initDate(it.data?.items)
                    binding.lineLoading.visibility = View.GONE
                    binding.rec.visibility = View.VISIBLE
                }
                Status.ERROR -> checkInternet()
                else -> {
                    binding.lineLoading.visibility = View.VISIBLE
                    binding.rec.visibility = View.GONE
                }
            }
        }
    }

    private fun initDate(items: List<Item>?) {
        binding.rec.adapter = items?.let { PlaylistAdapter(it, this, requireContext()) }
    }

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentPlaylistsBinding {
        return FragmentPlaylistsBinding.inflate(inflater)
    }

    override fun clickOnItem(any: Any) {
        if (any is Item) navigate(PlaylistsFragmentDirections
            .actionPlaylistsFragmentToPlaylistFragment(any.id))
    }

    override fun onStart() {
        super.onStart()
        initViewModel()
    }
}