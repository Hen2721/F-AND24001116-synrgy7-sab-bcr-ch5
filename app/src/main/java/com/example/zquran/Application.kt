package com.example.zquran

import android.app.Application
import com.example.zquran.di.Provider

class Application: Application() {
    lateinit var provider: Provider

    override fun onCreate() {
        super.onCreate()
        provider = Provider(applicationContext)
    }
}