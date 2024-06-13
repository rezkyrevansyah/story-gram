package com.dicoding.storysub.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.activity.viewModels
import com.dicoding.storysub.RootActivity
import com.dicoding.storysub.data.dataclass.User
import com.dicoding.storysub.data.response.ApiResponse
import com.dicoding.storysub.databinding.ActivityLoginBinding
import com.dicoding.storysub.factory.AuthViewModelFactory
import com.dicoding.storysub.viewmodel.LoginViewModel

class LoginActivity : RootActivity<ActivityLoginBinding>() {

    private val viewModel by viewModels<LoginViewModel> {
        AuthViewModelFactory.getInstance(this)
    }

    override fun getViewBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun setUI() {
        setupAnimation()
        setupPasswordVisibilityToggle()
        setupClickListeners()
    }

    override fun setProcess() {
    }

    override fun setObserve() {
    }

    private fun setupAnimation() {
        ObjectAnimator.ofFloat(binding.iconLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val viewsToFadeIn = listOf(
            binding.tvLoginDescription,
            binding.CVEmail,
            binding.PasswordLogin,
            binding.seePassword,
            binding.btnLogin,
            binding.btnRegister,
            binding.tvRegistDescription
        )

        AnimatorSet().apply {
            playSequentially(viewsToFadeIn.map {
                ObjectAnimator.ofFloat(it, View.ALPHA, 1f).setDuration(300)
            })
            start()
        }
    }

    private fun setupPasswordVisibilityToggle() {
        binding.seePassword.setOnCheckedChangeListener { _, isChecked ->
            binding.PasswordLogin.transformationMethod = if (isChecked) {
                HideReturnsTransformationMethod.getInstance()
            } else {
                PasswordTransformationMethod.getInstance()
            }
            binding.PasswordLogin.text?.let { binding.PasswordLogin.setSelection(it.length) }
        }
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            login()
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun login() {
        val email = binding.CVEmail.text.toString()
        val password = binding.PasswordLogin.text.toString()

        viewModel.login(email, password).observe(this@LoginActivity) { response ->
            when (response) {
                is ApiResponse.Loading -> showLoading(true)
                is ApiResponse.Success -> handleLoginSuccess(response.data.loginResult?.token)
                is ApiResponse.Error -> handleLoginError(response.error)
            }
        }
    }

    private fun handleLoginSuccess(token: String?) {
        showLoading(false)
        token?.let {
            viewModel.saveSession(User(binding.CVEmail.text.toString(), it, true))
        }
        startActivity(Intent(this@LoginActivity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
        finish()
    }

    private fun handleLoginError(error: String?) {
        showLoading(false)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}
