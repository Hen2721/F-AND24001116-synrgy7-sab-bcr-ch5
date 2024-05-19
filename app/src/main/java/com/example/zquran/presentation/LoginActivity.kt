package com.example.zquran.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.zquran.Application
import com.example.zquran.databinding.ActivityLoginBinding
import com.example.zquran.di.ViewModelFactory
import com.example.zquran.domain.Login
import com.example.zquran.presentation.helper.Helper
import com.example.zquran.presentation.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance((application as Application).provider)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        loginViewModel.checkAuth()

        observeLogin()

        binding.loginButton.setOnClickListener { handleLogin() }
        binding.loginRegisterText2.setOnClickListener { RegisterActivity.startActivity(this) }
    }

    private fun observeLogin() {
        loginViewModel.error.observe(this, ::handleError)
        loginViewModel.loading.observe(this, ::handleLoading)
        loginViewModel.authentication.observe(this, ::handleAuthentication)
        loginViewModel.login.observe(this, ::handleSuccess)
    }

    private fun handleError(error: String) {
        Helper.showToast(this, this, error, isSuccess = false)
    }

    private fun handleLoading(loading: Boolean) {
        val visibility = if (loading) View.VISIBLE else View.GONE
        binding.loginLoadingLayout.visibility = visibility
    }

    private fun handleSuccess(message: String) {
        Helper.showToast(this, this, message, isSuccess = true)
    }

    private fun handleLogin() {
        val username = binding.loginTiUsername.text.toString()
        val password = binding.loginTiPassword.text.toString()

        loginViewModel.login(
            Login(
                username = username,
                password = password
            )
        )
    }

    private fun handleAuthentication(token: String) {
        if (token.isNotEmpty() && token.isNotBlank()) {
            loginViewModel.setToken(token)
            HomeActivity.startActivity(this)
            this.finish()
        }
    }
}