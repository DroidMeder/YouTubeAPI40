package kg.geekteck.youtubeapi40.ui.playlists

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kg.geekteck.youtubeapi40.R
import kg.geekteck.youtubeapi40.databinding.ItemBinding
import kg.geekteck.youtubeapi40.extensions.chooseTheMostQualityImage
import kg.geekteck.youtubeapi40.extensions.glideSetter
import kg.geekteck.youtubeapi40.interfaces.SomethingClicked
import kg.geekteck.youtubeapi40.models.Item

class PlaylistAdapter(private var list: List<Item>, private var onItemClick: SomethingClicked,
    private val context: Context) : RecyclerView.Adapter<PlaylistAdapter.PlaylistsHolder>() {


    class PlaylistsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemBinding.bind(itemView)
        fun bind(i: Item, context: Context) = with(binding) {
            val uri = context.chooseTheMostQualityImage(i)
            context.glideSetter(uri, ivPlaylists)
            tvPlaylistTitle.text = i.snippet.title
            val itemCount = i.contentDetails.itemCount.toString()
            tvCountOfVideos.text = context.getString(R.string.videos, itemCount)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return PlaylistsHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistsHolder, position: Int) {
        holder.bind(list[position], context)
        holder.itemView.setOnClickListener{
            onItemClick.clickOnItem(list[position])
        }
    }

    override fun getItemCount(): Int = list.size
}