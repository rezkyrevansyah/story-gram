package com.dicoding.storysub.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.dicoding.storysub.RootActivity
import com.dicoding.storysub.R
import com.dicoding.storysub.data.response.ApiResponse
import com.dicoding.storysub.databinding.ActivityRegisterBinding
import com.dicoding.storysub.factory.AuthViewModelFactory
import com.dicoding.storysub.viewmodel.RegisterViewModel

class RegisterActivity : RootActivity<ActivityRegisterBinding>() {

    private val viewModel by viewModels<RegisterViewModel> {
        AuthViewModelFactory.getInstance(this)
    }

    override fun getViewBinding(): ActivityRegisterBinding {
        return ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun setUI() {
        setupAnimation()
    }

    override fun setProcess() {
        setupClickListeners()
        setupPasswordVisibilityToggle()
    }

    override fun setObserve() {
    }

    private fun setupAnimation() {
        ObjectAnimator.ofFloat(binding.iconRegist, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val viewsToFadeIn = listOf(
            binding.tvRegist,
            binding.RegistName,
            binding.RegistEmail,
            binding.RegistPassword,
            binding.seeRegistPassword,
            binding.btnRegistAccount
        )

        AnimatorSet().apply {
            playSequentially(viewsToFadeIn.map {
                ObjectAnimator.ofFloat(it, View.ALPHA, 1f).setDuration(300)
            })
            start()
        }
    }

    private fun setupClickListeners() {
        binding.btnRegistAccount.setOnClickListener {
            register()
        }
    }

    private fun setupPasswordVisibilityToggle() {
        binding.seeRegistPassword.setOnCheckedChangeListener { _, isChecked ->
            binding.RegistPassword.transformationMethod = if (isChecked) {
                HideReturnsTransformationMethod.getInstance()
            } else {
                PasswordTransformationMethod.getInstance()
            }
            binding.RegistPassword.text?.let { binding.RegistPassword.setSelection(it.length) }
        }
    }

    private fun register() {
        val name = binding.RegistName.text.toString()
        val email = binding.RegistEmail.text.toString()
        val password = binding.RegistPassword.text.toString()

        viewModel.register(name, email, password).observe(this@RegisterActivity) { response ->
            when (response) {
                is ApiResponse.Loading -> showLoading(true)
                is ApiResponse.Success -> handleRegistrationSuccess()
                is ApiResponse.Error -> handleRegistrationError(response.error)
            }
        }
    }

    private fun handleRegistrationSuccess() {
        showLoading(false)
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.registration_success))
            setMessage(getString(R.string.registration_message))
            setPositiveButton(getString(R.string.next)) { _, _ ->
                startActivity(Intent(this@RegisterActivity, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                })
                finish()
            }
            create()
            show()
        }
    }

    private fun handleRegistrationError(error: String?) {
        showLoading(false)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
