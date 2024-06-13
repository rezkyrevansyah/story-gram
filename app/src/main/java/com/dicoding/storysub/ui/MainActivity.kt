package com.dicoding.storysub.ui

import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storysub.RootActivity
import kotlinx.coroutines.launch
import com.dicoding.storysub.adapter.StoryListAdapter
import com.dicoding.storysub.databinding.ActivityMainBinding
import com.dicoding.storysub.factory.StoryViewModelFactory
import com.dicoding.storysub.viewmodel.MainViewModel

class MainActivity : RootActivity<ActivityMainBinding>() {

    private val viewModel by viewModels<MainViewModel> {
        StoryViewModelFactory.getInstance(this)
    }

    private lateinit var storyListAdapter: StoryListAdapter

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
    }

    override fun setUI() {
        setupRecyclerView()
        setStoryData()
    }

    override fun setProcess() {
        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    override fun setObserve() {
        observeSession()
    }

    private fun setupRecyclerView() {
        storyListAdapter = StoryListAdapter()
        binding.rvStory.adapter = storyListAdapter
        binding.rvStory.layoutManager = LinearLayoutManager(this)
    }

    private fun setStoryData() {
        viewModel.story.observe(this) { stories ->
            lifecycleScope.launch {
                storyListAdapter.submitData(stories)
            }
        }
    }

    private fun observeSession() {
        viewModel.getSession().observe(this) { user ->
            if (user.token.isEmpty()) {
                startActivity(Intent(this, OnBoardingActivity::class.java))
                finish()
            }
        }
    }
}
