package com.dicoding.storysub.ui

import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.dicoding.storysub.RootActivity
import com.dicoding.storysub.data.response.ApiResponse
import com.dicoding.storysub.data.response.Story
import com.dicoding.storysub.databinding.ActivityDetailBinding
import com.dicoding.storysub.utils.setImageUrl
import com.dicoding.storysub.factory.StoryViewModelFactory
import com.dicoding.storysub.viewmodel.DetailViewModel

class DetailActivity : RootActivity<ActivityDetailBinding>() {
    override fun getViewBinding(): ActivityDetailBinding {
        return ActivityDetailBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<DetailViewModel> {
        StoryViewModelFactory.getInstance(this)
    }

    private lateinit var imageUrl: String

    override fun setUI() {
    }

    override fun setProcess() {
        observeSession()
    }

    override fun setObserve() {
    }

    private fun observeSession() {
        viewModel.getSession().observe(this) { user ->
            if (user.token.isNotEmpty()) {
                val storyId = intent.getStringExtra(EXTRA_ID)
                if (storyId != null) {
                    observeStoryDetail(storyId)
                }
            } else {
                showToast("Can't get ${user.email} token")
            }
        }
    }

    private fun observeStoryDetail(storyId: String) {
        viewModel.getStoryDetail(storyId).observe(this) { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    showLoading(true)
                }

                is ApiResponse.Success -> {
                    viewModel.story.observe(this) { story ->
                        showLoading(false)
                        storyData(story)
                        Log.d("Success", "Success get detail")
                    }
                    viewModel.getDetail(storyId)
                }

                is ApiResponse.Error -> {
                    showLoading(false)
                    showToast(response.error)
                }
            }
        }
    }

    private fun storyData(story: Story) {
        binding.imageDetailStory.setImageUrl(story.photoUrl)
        binding.tvDetailName.text = story.name
        binding.tvDetailDesc.text = story.description
        imageUrl = story.photoUrl!!
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_PHOTO = "extra_photo"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TITLE = "extra_title"
    }
}
