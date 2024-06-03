package com.dicoding.userrespon

import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutor {
    val mainThread: Executor = MainThreadExecutor()
    val networkIO: Executor = Executors.newFixedThreadPool(3)
    val diskIO: Executor = Executors.newSingleThreadExecutor()

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}