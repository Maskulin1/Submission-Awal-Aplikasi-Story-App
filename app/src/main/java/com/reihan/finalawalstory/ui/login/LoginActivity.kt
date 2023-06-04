package com.reihan.finalawalstory.ui.login

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
import com.reihan.finalawalstory.databinding.ActivityLoginBinding
import com.reihan.finalawalstory.model.LoginViewModel
import com.reihan.finalawalstory.model.UserModel
import com.reihan.finalawalstory.remote.data.LoginResult
import com.reihan.finalawalstory.remote.preferences.ViewModelPreferences
import com.reihan.finalawalstory.ui.main.MainActivity
import com.reihan.finalawalstory.ui.register.RegisterActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModel()
    private val userViewModel: ViewModelPreferences by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.hyperlinkRegisterButton.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.emailEditText.error == null) {
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

        binding.loginButton.setOnClickListener {
            viewModel.userLogin(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
        }

        viewModel.loginResult.observe(this) {
            if (it != null) {
                setLoginState(it)
            }
        }

        viewModel.showLoading.observe(this) {
            showLoading(it)
        }

        enableButton()
        animatedPlay()
    }

    private fun enableButton() {
        binding.loginButton.isEnabled = false
    }

    private fun animatedPlay() {
        val imageAnimator = ObjectAnimator.ofFloat(binding.imageViewAnimated, View.TRANSLATION_X, -45f, 45f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }
        imageAnimator.start()

        val fadeInDuration = 500L
        val labelAnimator = ObjectAnimator.ofFloat(binding.greetingViewLabel, View.ALPHA, 1f).setDuration(fadeInDuration)
        val appViewAnimator = ObjectAnimator.ofFloat(binding.appViewLabel, View.ALPHA, 1f).setDuration(fadeInDuration)
        val loginAnimator = ObjectAnimator.ofFloat(binding.loginAuthenticationLabel, View.ALPHA, 1f).setDuration(fadeInDuration)
        val emailAnimator = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(fadeInDuration)
        val passwordAnimator = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(fadeInDuration)
        val buttonAnimator = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(fadeInDuration)
        val layoutAnimator = ObjectAnimator.ofFloat(binding.linearLayoutLoginFragment, View.ALPHA, 1f).setDuration(fadeInDuration)

        val togetherAnimator = AnimatorSet().apply {
            playTogether(buttonAnimator, layoutAnimator)
        }

        AnimatorSet().apply {
            playSequentially(labelAnimator, appViewAnimator, loginAnimator, emailAnimator, passwordAnimator, togetherAnimator)
            start()
        }
    }

    private fun setLoginState(loginResult: LoginResult) {
        val userModel = UserModel(loginResult.userId, loginResult.name, loginResult.token)
        userViewModel.saveUserData(userModel)
        Toast.makeText(this, "Welcome ${userModel.name}", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun isEnable() {
        val email = binding.emailEditText.text
        val password = binding.passwordEditText.text

        binding.loginButton.isEnabled = !email.isNullOrEmpty() && !password.isNullOrEmpty()
    }

    private fun showLoading(it: Boolean) {
        binding.progressBar.isVisible = it
        binding.loginButton.isEnabled = !it
    }
}