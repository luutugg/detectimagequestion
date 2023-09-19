package com.example.detachimage.presentation

import android.app.Application

private var app: Application? = null

private fun setApplication(application: Application){
    app = application
}

fun getApplication() : Application= app!!

class DetachApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        setApplication(this)
    }
}
