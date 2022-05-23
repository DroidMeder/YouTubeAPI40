package kg.geekteck.youtubeapi40.ui.playlist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kg.geekteck.youtubeapi40.databinding.ItemPlaylistBinding
import kg.geekteck.youtubeapi40.core.ui.extensions.chooseTheMostQualityImage
import kg.geekteck.youtubeapi40.core.ui.extensions.glideSetter
import kg.geekteck.youtubeapi40.core.ui.extensions.replacer
import kg.geekteck.youtubeapi40.core.ui.interfaces.SomethingClicked
import kg.geekteck.youtubeapi40.data.remote.models.Item
import kg.geekteck.youtubeapi40.data.remote.models.Video

class PlaylistAdapter(
    private var list: List<Item>,
    private val context: Context,
    private var videosDuration: List<Video>,
    private var clicked: SomethingClicked
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistHolder>() {


    class PlaylistHolder(private val binding: ItemPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(i: Item, context: Context, videos: List<Video>) = with(binding) {
            val uri = context.chooseTheMostQualityImage(i)
            context.glideSetter(uri, ivPlaylist)
            tvPlaylistTitle.text = i.snippet.title
            videos.forEach {
                if (i.contentDetails.videoId == it.id){
                    binding.tvDurationOfVideo.text = replacer(it)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistHolder {
        return PlaylistHolder(ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: PlaylistHolder, position: Int) {
        holder.bind(list[position], context, videosDuration)
        holder.itemView.setOnClickListener {
            clicked.clickOnItem(list[position])
        }
    }

    override fun getItemCount(): Int = list.size
}