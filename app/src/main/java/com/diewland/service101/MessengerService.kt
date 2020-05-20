package com.diewland.service101

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log

// https://developer.android.com/guide/components/bound-services
// bound service support 2 ways communication
//  * https://android.googlesource.com/platform/development/+/master/samples/ApiDemos/src/com/example/android/apis/app/MessengerService.java
//  * https://android.googlesource.com/platform/development/+/master/samples/ApiDemos/src/com/example/android/apis/app/MessengerServiceActivities.java

/** Command to the service to display a message  */
private const val MSG_SAY_HELLO = 1

class MessengerService : Service() {

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    private lateinit var mMessenger: Messenger

    /**
     * Handler of incoming messages from clients.
     */
    internal class IncomingHandler(
        context: Context,
        private val applicationContext: Context = context.applicationContext
    ) : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_SAY_HELLO ->
                    Log.d(Const.TAG, "Hello from MessengerServer")
                else -> super.handleMessage(msg)
            }
        }
    }

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    override fun onBind(intent: Intent): IBinder? {
        Log.d(Const.TAG, "binding from MessengerServer")
        mMessenger = Messenger(IncomingHandler(this))
        return mMessenger.binder
    }

}