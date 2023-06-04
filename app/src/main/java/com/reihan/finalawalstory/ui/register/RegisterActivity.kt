package com.reihan.finalawalstory.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.reihan.finalawalstory.databinding.ActivityRegisterBinding
import com.reihan.finalawalstory.model.RegisterViewModel
import com.reihan.finalawalstory.ui.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.hyperlinkLoginButton.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            viewModel.userRegister(name, email, password)
            viewModel.resultRegister.observe(this) {
                if (it.error) {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
            viewModel.isLoading.observe(this) {
                isLoading(it)
            }
        }

        enableRegisterButton()
        animatedPlay()
        enableButton()
    }

    private fun enableButton() {
        binding.registerButton.isEnabled = false
    }

    private fun enableRegisterButton() {
        binding.nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.nameEditText.error == null) {
                    isEnable()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.nameEditText.error == null) {
                    isEnable()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.passwordEditText.error == null) {
                    isEnable()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun animatedPlay() {
        val imageAnimator =
            ObjectAnimator.ofFloat(binding.imageViewAnimated, View.TRANSLATION_X, -45f, 45f).apply {
                duration = 6000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }
        imageAnimator.start()

        val fadeInDuration = 500L
        val labelAnimator = ObjectAnimator.ofFloat(binding.greetingViewLabel, View.ALPHA, 1f)
            .setDuration(fadeInDuration)
        val appViewAnimator =
            ObjectAnimator.ofFloat(binding.appViewLabel, View.ALPHA, 1f).setDuration(fadeInDuration)
        val loginAnimator = ObjectAnimator.ofFloat(binding.loginAuthenticationLabel, View.ALPHA, 1f)
            .setDuration(fadeInDuration)
        val nameAnimator =
            ObjectAnimator.ofFloat(binding.nameEditText, View.ALPHA, 1f).setDuration(fadeInDuration)
        val emailAnimator = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f)
            .setDuration(fadeInDuration)
        val passwordAnimator = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f)
            .setDuration(fadeInDuration)
        val buttonAnimator = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f)
            .setDuration(fadeInDuration)
        val layoutAnimator =
            ObjectAnimator.ofFloat(binding.linearLayoutLoginFragment, View.ALPHA, 1f)
                .setDuration(fadeInDuration)

        val togetherAnimator = AnimatorSet().apply {
            playTogether(buttonAnimator, layoutAnimator)
        }

        AnimatorSet().apply {
            playSequentially(
                labelAnimator,
                appViewAnimator,
                loginAnimator,
                nameAnimator,
                emailAnimator,
                passwordAnimator,
                togetherAnimator
            )
            start()
        }
    }

    fun isEnable() {
        val name = binding.nameEditText.text
        val email = binding.emailEditText.text
        val password = binding.passwordEditText.text
        binding.registerButton.isEnabled =
            !name.isNullOrEmpty() && !email.isNullOrEmpty() && !password.isNullOrEmpty()
                    && binding.nameEditText.error == null && binding.emailEditText.error == null
                    && binding.passwordEditText.error == null

        if (!binding.registerButton.isEnabled) {
            binding.registerButton.alpha = 0.5f
        } else {
            binding.registerButton.alpha = 1f
        }
    }

    private fun isLoading(state: Boolean) {
        binding.progressBar.isVisible = state
    }
}