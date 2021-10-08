package com.phoenix.appstory.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.google.android.exoplayer2.Player
import com.phoenix.appstory.R
import com.phoenix.appstory.databinding.StoryListItemBinding
import com.phoenix.appstory.model.Story
import kohii.v1.core.Playback
import kohii.v1.exoplayer.Kohii

class NestedStoryAdapter(
    private val parentPosition: Int,
    private val stories: List<Story>,
    private val context: Context,
    private val kohii: Kohii
) : Adapter<NestedStoryAdapter.StoryViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        val item = stories[position]
        return item.hashCode()
            .toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        return StoryViewHolder(
            StoryListItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories[position]

        val itemTag = "NEST::$parentPosition::${story.url}::$position"
        holder.storyBinding.thumbNail.load(R.drawable.thumbnail)

        kohii.setUp(story.url) {
            tag = itemTag
            preload = true
            repeatMode = Player.REPEAT_MODE_ONE
            controller = object : Playback.Controller {
                override fun kohiiCanPause() = true

                override fun kohiiCanStart() = true

                override fun setupRenderer(playback: Playback, renderer: Any?) {
                    holder.storyBinding.root.setOnClickListener {
                        val playable = playback.playable ?: return@setOnClickListener

                        if (playable.isPlaying()) {
                            playback.manager.pause(playable)
                        } else playback.manager.play(playable)
                    }
                }
            }

            artworkHintListener = object : Playback.ArtworkHintListener {
                override fun onArtworkHint(
                    playback: Playback,
                    shouldShow: Boolean,
                    position: Long,
                    state: Int
                ) {
                    holder.storyBinding.thumbNail.isVisible =
                        playback.playable?.isPlaying() == false
                }
            }
        }.bind(holder.storyBinding.playerView)

        /* kohii.setUp(story.url) {
             tag = "video::$position"
             threshold = 0.5F
             preload = true
             repeatMode = Player.REPEAT_MODE_ONE
             artworkHintListener = object : Playback.ArtworkHintListener {
                 override fun onArtworkHint(
                     playback: Playback,
                     shouldShow: Boolean,
                     position: Long,
                     state: Int
                 ) {
                 }
             }

             controller = object : Playback.Controller {
                 override fun kohiiCanPause() = true

                 override fun kohiiCanStart() = true

                 override fun setupRenderer(playback: Playback, renderer: Any?) {
                     holder.storyBinding.root.setOnClickListener {
                         val playable = playback.playable ?: return@setOnClickListener

                         if (playable.isPlaying()) {
                             playback.manager.pause(playable)
                         } else playback.manager.play(playable)
                     }
                 }

                 override fun teardownRenderer(playback: Playback, renderer: Any?) {
                     holder.storyBinding.root.setOnClickListener(null)
                 }
             }
         }.bind(holder.storyBinding.playerView) {

         }*/
    }

    override fun getItemCount() = stories.size

    override fun onViewRecycled(holder: StoryViewHolder) {
        super.onViewRecycled(holder)

        // holder.onRecycled(true)
    }


    override fun onFailedToRecycleView(holder: StoryViewHolder): Boolean {
        kohii.cancel(holder.storyBinding.playerView)
        return super.onFailedToRecycleView(holder)
    }

    inner class StoryViewHolder(val storyBinding: StoryListItemBinding) :
        ViewHolder(storyBinding.root) {

        fun bind(story: Story) {

        }
    }
}

/*
*    const val VIDEO2 =
        "https://test-videos.co.uk/vids/jellyfish/mp4/h264/360/Jellyfish_360_10s_2MB.mp4"
    const val VIDEO_1 = "https://www.rmp-streaming.com/media/big-buck-bunny-360p.mp4"
* */