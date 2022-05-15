package kg.geekteck.youtubeapi40.ui.playlists

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kg.geekteck.youtubeapi40.R
import kg.geekteck.youtubeapi40.databinding.ItemBinding
import kg.geekteck.youtubeapi40.extensions.chooseTheMostQualityImage
import kg.geekteck.youtubeapi40.extensions.glideSetter
import kg.geekteck.youtubeapi40.interfaces.SomethingClicked
import kg.geekteck.youtubeapi40.models.Item

class PlaylistsAdapter(
    private var list: List<Item>, private var onItemClick: SomethingClicked,
    private val context: Context,
) : RecyclerView.Adapter<PlaylistsAdapter.PlaylistsHolder>() {


    class PlaylistsHolder(private val binding: ItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(i: Item, context: Context) = with(binding) {
            val uri = context.chooseTheMostQualityImage(i)
            context.glideSetter(uri, ivPlaylists)
            tvPlaylistTitle.text = i.snippet.title
            val itemCount = i.contentDetails.itemCount.toString()
            tvCountOfVideos.text = context.getString(R.string.videos, itemCount)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsHolder {
        return PlaylistsHolder(ItemBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: PlaylistsHolder, position: Int) {
        holder.bind(list[position], context)
        holder.itemView.setOnClickListener {
            onItemClick.clickOnItem(list[position])
        }
    }

    override fun getItemCount(): Int = list.size
}