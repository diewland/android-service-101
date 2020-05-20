package com.diewland.service101

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log

/*
    simple service  - https://www.youtube.com/watch?v=GAOH7XTW7BU
*/

class MyService : Service() {

    var num = 0
    lateinit var handler: Handler

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        /*
        The system invokes this method by calling startService() when another component (such as an activity)
        requests that the service be started. When this method executes, the service is started
        and can run in the background indefinitely. If you implement this, it is your responsibility
        to stop the service when its work is complete by calling stopSelf() or stopService().
        If you only want to provide binding, you don't need to implement this method.
        */
        Log.d(Const.TAG, "[$startId] start service")

        handler = Handler()
        count(startId)

        return START_STICKY // service restart after service is closed
    }

    override fun onCreate() {
        /*
        The system invokes this method to perform one-time setup procedures when the service is
        initially created (before it calls either onStartCommand() or onBind()).
        If the service is already running, this method is not called.
        */
        super.onCreate()

        Log.d(Const.TAG, "create service")
    }

    override fun onDestroy() {
        /*
        The system invokes this method when the service is no longer used and is being destroyed.
        Your service should implement this to clean up any resources such as threads,
        registered listeners, or receivers. This is the last call that the service receives.
        */
        super.onDestroy()

        num = 0
        handler.removeCallbacksAndMessages(null)

        Log.d(Const.TAG, "stop service")
    }

    override fun onBind(p0: Intent?): IBinder? {
        /*
        The system invokes this method by calling bindService() when another component wants to bind
        with the service (such as to perform RPC). In your implementation of this method, you must
        provide an interface that clients use to communicate with the service by returning an IBinder.
        You must always implement this method; however, if you don't want to allow binding,
        you should return null.
        */
        TODO("not implemented")
        // return null
    }

    private fun count (id: Int) {
        handler.postDelayed({
            num += 1
            Log.d(Const.TAG, "[$id] count $num")
            if (num >= 10) {
                Log.d(Const.TAG, "[$id] reached 10, stop itself")
                stopSelf(id)
            }
            else {
                count(id)
            }
        }, 1000)
    }
}