package com.diewland.service101

import android.app.IntentService
import android.content.Intent
import android.util.Log

/*
    simple service  - https://www.youtube.com/watch?v=GAOH7XTW7BU
*/

/**
 * A constructor is required, and must call the super [android.app.IntentService.IntentService]
 * constructor with a name for the worker thread.
 */
class MyIntentService : IntentService("MyIntentService") {

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    override fun onHandleIntent(p0: Intent?) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.
        try {

            Log.d(Const.TAG, "do 3 seconds process..")
            Thread.sleep(3000)
            Log.d(Const.TAG, "process done")

        } catch (e: InterruptedException) {
            // Restore interrupt status.
            Thread.currentThread().interrupt()
        }
    }

}