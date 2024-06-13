package com.dicoding.storysub.ui

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.dicoding.storysub.R
import com.dicoding.storysub.RootActivity
import com.dicoding.storysub.data.response.ApiResponse
import com.dicoding.storysub.databinding.ActivityAddBinding
import com.dicoding.storysub.utils.getImageUri
import com.dicoding.storysub.utils.uriToFile
import com.dicoding.storysub.factory.StoryViewModelFactory
import com.dicoding.storysub.viewmodel.AddViewModel

class AddActivity : RootActivity<ActivityAddBinding>() {

    private var currentImageUri: Uri? = null
    private val viewModel by viewModels<AddViewModel> {
        StoryViewModelFactory.getInstance(this)
    }

    override fun getViewBinding(): ActivityAddBinding {
        return ActivityAddBinding.inflate(layoutInflater)
    }

    override fun setUI() {
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.btnPostStory.setOnClickListener {
            currentImageUri?.let { uri ->
                val description = binding.tvDes.text.toString()
                uploadStory(uri, description)
                showToast(getString(R.string.story_uploaded))
            } ?: showToast(getString(R.string.select_image))
        }
    }

    override fun setProcess() {}

    override fun setObserve() {}

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) showImage()
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            currentImageUri = it
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.also {
            binding.imageStoryUpload.setImageURI(it)
        }
    }

    private fun uploadStory(uri: Uri, description: String) {
        viewModel.getSession()
        viewModel.uploadStory(uriToFile(uri, this), description).observe(this) { response ->
            when (response) {
                is ApiResponse.Loading -> showLoading(true)
                is ApiResponse.Success -> handleSuccess()
                is ApiResponse.Error -> handleError(response.error)
            }
        }
    }

    private fun handleSuccess() {
        val intent = Intent(this@AddActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun handleError(errorMessage: String) {
        showLoading(false)
        showToast(errorMessage)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
