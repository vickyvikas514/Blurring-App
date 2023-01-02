package com.example.background.workers

import android.content.Context
import android.content.ContextParams
import android.graphics.BitmapFactory
import android.net.Uri
import android.nfc.Tag
import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.KEY_IMAGE_URI
import com.example.background.R
import com.example.background.TAG_OUTPUT

class BlurWorker(ctx: Context, params: WorkerParameters) : Worker(ctx,params) {

    override fun doWork(): Result {

        val appContext = applicationContext

        val resourceUri = inputData.getString(KEY_IMAGE_URI)
        makeStatusNotification("Blurrjng image", appContext)

        return try {
            //checking that resoureuri !=Null
        if(TextUtils.isEmpty(resourceUri)){
            Log.e(TAG_OUTPUT,"Invalid input uri")
            throw IllegalArgumentException("Invalid input uri")
        }
            val resolver = appContext.contentResolver
            val picture = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri))
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