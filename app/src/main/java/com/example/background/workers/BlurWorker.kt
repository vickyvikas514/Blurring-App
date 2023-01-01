package com.example.background.workers

import android.content.Context
import android.content.ContextParams
import android.graphics.BitmapFactory
import android.nfc.Tag
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.R
import com.example.background.TAG_OUTPUT

class BlurWorker(ctx: Context, params: WorkerParameters) : Worker(ctx,params) {

    override fun doWork(): Result {

        val appContext = applicationContext
        makeStatusNotification("Blurrjng image", appContext)

        return try {
            val picture = BitmapFactory.decodeResource(
                appContext.resources,
                R.drawable.android_cupcake
            )

            val output = blurBitmap(picture, appContext)

            // Write bitmap to a temp file
            val outputUri = writeBitmapToFile(appContext, output)

            makeStatusNotification("Output is $outputUri", appContext)

            Result.success()
        } catch  (throwable: Throwable) {
           Log.e(TAG_OUTPUT,"Error applying blur")
            Result.failure()
        }

    }
}