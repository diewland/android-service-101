package com.diewland.service101

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MyService2: Service() {

    var t: Thread? = null
    var shouldRun = true

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        var num = 0
        t = Thread(Runnable {
            Log.d(Const.TAG, "hello!")
            while (shouldRun) {
                num += 1
                Log.d(Const.TAG, "count -> $num")
                Thread.sleep(1_000)
                if (num == 10) shouldRun = false
            }
            Log.d(Const.TAG, "goodbye!")
        })
        t!!.start()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        shouldRun = false
        t!!.join()
        Log.d(Const.TAG, "destroyed")
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

}