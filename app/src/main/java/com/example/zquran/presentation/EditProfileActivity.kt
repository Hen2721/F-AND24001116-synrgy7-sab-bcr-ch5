package com.example.zquran.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.zquran.Application
import com.example.zquran.databinding.ActivityEditProfileBinding
import com.example.zquran.di.ViewModelFactory
import com.example.zquran.domain.Profile
import com.example.zquran.presentation.helper.Helper
import com.example.zquran.presentation.viewmodel.ProfileViewModel
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton

class EditProfileActivity : AppCompatActivity() {
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, EditProfileActivity::class.java))
        }
    }

    private lateinit var binding: ActivityEditProfileBinding

    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance((application as Application).provider)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        profileViewModel.getProfile()

        observeProfile()
        handleUpdateProfile()
    }

    private fun observeProfile() {
        profileViewModel.error.observe(this, ::handleError)
        profileViewModel.loading.observe(this, ::handleLoading)
        profileViewModel.dataLoading.observe(this, ::handleDataLoading)
        profileViewModel.profileData.observe(this, ::handleGetProfile)
        profileViewModel.message.observe(this, ::handleSuccess)
    }

    private fun handleError(error: String) {
        Helper.showToast(this, this, error, isSuccess = false)
    }

    private fun handleSuccess(message: String) {
        Helper.showToast(this, this, message, isSuccess = true)
        ProfileActivity.startActivity(this)
        this.finish()
    }

    private fun handleLoading(loading: Boolean) {
        val visibility = if (loading) View.VISIBLE else View.GONE
        binding.profileLoadingLayout.visibility = visibility
    }

    private fun handleDataLoading(loading: Boolean) {
        if (loading) {
            binding.editProfileLayout.loadSkeleton()
        } else {
            Handler().postDelayed({
                binding.editProfileLayout.hideSkeleton()
            }, 2000)
        }
    }

    private fun handleGetProfile(user: Profile) {
        binding.profileTiName.setText(user.name)
        binding.profileTiUsername.setText(user.username)
        binding.profileTiEmail.setText(user.email)
        binding.profileTiPhone.setText(user.phone)
    }

    private fun handleUpdateProfile() {
        binding.profileButtonEdit.setOnClickListener {
            val name = binding.profileTiName.text.toString()
            val username = binding.profileTiUsername.text.toString()
            val email = binding.profileTiEmail.text.toString()
            val phone = binding.profileTiPhone.text.toString()

            profileViewModel.updateProfile(Profile(
                name = name,
                username = username,
                email = email,
                phone = phone
            ))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            this.finish()
        }
        return super.onOptionsItemSelected(item);
    }
}