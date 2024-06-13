package com.dicoding.storysub.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.view.View
import com.dicoding.storysub.RootActivity
import com.dicoding.storysub.databinding.ActivityOnBoardingBinding

class OnBoardingActivity : RootActivity<ActivityOnBoardingBinding>() {

    override fun getViewBinding(): ActivityOnBoardingBinding {
        return ActivityOnBoardingBinding.inflate(layoutInflater)
    }

    override fun setUI() {
        playAnimation()
    }

    override fun setProcess() {
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun setObserve() {}

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.welcomeIcon, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(150)
        val signup = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(150)
        val title = ObjectAnimator.ofFloat(binding.tagline, View.ALPHA, 1f).setDuration(150)
        val desc = ObjectAnimator.ofFloat(binding.description, View.ALPHA, 1f).setDuration(150)

        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }

        AnimatorSet().apply {
            playSequentially(title, desc, together)
            start()
        }
    }
}