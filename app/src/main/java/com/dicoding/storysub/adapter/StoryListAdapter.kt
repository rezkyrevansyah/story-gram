package com.dicoding.storysub.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.storysub.data.response.ListStoryItem
import com.dicoding.storysub.databinding.ItemStoryBinding
import com.dicoding.storysub.ui.DetailActivity
import com.dicoding.storysub.utils.setImageUrl

class StoryListAdapter :
    PagingDataAdapter<ListStoryItem, StoryListAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    class StoryViewHolder(private val itemBinding: ItemStoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun setUpView(story: ListStoryItem) {
            itemBinding.tvStoryTitle.text = story.name
            itemBinding.tvStorySubtitle.text = story.description
            itemBinding.imgStoryPhoto.setImageUrl(story.photoUrl)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_TITLE, story.name)
                    putExtra(DetailActivity.EXTRA_PHOTO, story.photoUrl)
                    putExtra(DetailActivity.EXTRA_ID, story.id)
                }
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStoryBinding.inflate(inflater, parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        getItem(position)?.let { story ->
            holder.setUpView(story)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
