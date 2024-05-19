package com.example.zquran.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.zquran.Application
import com.example.zquran.databinding.ActivityGetStartedBinding
import com.example.zquran.di.ViewModelFactory
import com.example.zquran.presentation.viewmodel.LoginViewModel

class GetStartedActivity : AppCompatActivity() {
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, GetStartedActivity::class.java))
        }
    }

    private lateinit var binding: ActivityGetStartedBinding

    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance((application as Application).provider)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetStartedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        loginViewModel.checkAuth()

        loginViewModel.authentication.observe(this, ::handleAuthentication)

        binding.getStartedButton.setOnClickListener { LoginActivity.startActivity(this) }
    }

    private fun handleAuthentication(token: String) {
        if (token.isNotEmpty() && token.isNotBlank()) {
            loginViewModel.setToken(token)
            HomeActivity.startActivity(this)
            this.finish()
        }
    }
}