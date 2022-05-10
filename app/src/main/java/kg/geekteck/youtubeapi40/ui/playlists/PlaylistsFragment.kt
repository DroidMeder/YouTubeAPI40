package kg.geekteck.youtubeapi40.ui.playlists

import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kg.geekteck.youtubeapi40.base.BaseNavFragment
import kg.geekteck.youtubeapi40.base.BaseViewModel
import kg.geekteck.youtubeapi40.databinding.FragmentPlaylistsBinding
import kg.geekteck.youtubeapi40.extensions.InternetChecker
import kg.geekteck.youtubeapi40.extensions.NetworkStatus
import kg.geekteck.youtubeapi40.interfaces.SomethingClicked
import kg.geekteck.youtubeapi40.models.Item

class PlaylistsFragment : BaseNavFragment<FragmentPlaylistsBinding, BaseViewModel>(),
    SomethingClicked {
    override val viewModel: PlaylistsViewModel by lazy {
        ViewModelProvider(requireActivity())[PlaylistsViewModel::class.java]
    }

    override fun checkInternet() {
        super.checkInternet()
        InternetChecker(requireContext()).observe(this) {
            when (it) {
                NetworkStatus.Available -> initViewModel()
                NetworkStatus.Unavailable -> View.VISIBLE
            }
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        viewModel.getPlaylists().observe(this) {
            binding.rec.layoutManager = LinearLayoutManager(requireContext())
            val list = it.items as ArrayList<Item>
            binding.rec.adapter = PlaylistAdapter(list, this, requireContext())
        }
    }

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentPlaylistsBinding {
        return FragmentPlaylistsBinding.inflate(inflater)
    }

    override fun clickOnItem(any: Any) {
        if (any is Item) {
            navigate(PlaylistsFragmentDirections
                .actionPlaylistsFragmentToPlaylistFragment(any.id))
        }
    }
}