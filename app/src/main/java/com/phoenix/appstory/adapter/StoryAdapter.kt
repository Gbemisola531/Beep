package com.phoenix.appstory.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.phoenix.appstory.databinding.StoriesListItemBinding
import com.phoenix.appstory.model.StoryResponse
import kohii.v1.core.Manager
import kohii.v1.exoplayer.Kohii

class StoryAdapter(
    private val stories: List<StoryResponse>,
    private val context: Context,
    private val kohii: Kohii,
    private val manager: Manager
) : Adapter<StoryAdapter.StoryResponseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryResponseViewHolder {
        return StoryResponseViewHolder(
            StoriesListItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StoryResponseViewHolder, position: Int) {
        manager.addBucket(holder.storyBinding.storiesList)
        holder.bind(stories[position])
    }

    override fun getItemCount() = stories.size

    inner class StoryResponseViewHolder(val storyBinding: StoriesListItemBinding) :
        ViewHolder(storyBinding.root) {

        fun bind(story: StoryResponse) {
            storyBinding.name.text = story.name
            /*   val layoutManager = object : LinearLayoutManager(context) {
                   override fun canScrollHorizontally(): Boolean = false
               }

               storyBinding.name.setOnClickListener {
                   try {
                       layoutManager.scrollToPosition(layoutManager.findFirstCompletelyVisibleItemPosition() + 1)
                   } catch (exception: Exception) {
                       exception.printStackTrace()
                   }
               }*/

            val storyAdapter = NestedStoryAdapter(adapterPosition, story.stories, context, kohii)
            //  storyBinding.storiesList.layoutManager = layoutManager
            storyBinding.storiesList.adapter = storyAdapter
            storyBinding
        }
    }
}
