package com.diewland.service101

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/*
    https://developer.android.com/guide/components/services
    https://developer.android.com/guide/components/bound-services
    https://www.vogella.com/tutorials/AndroidServices/article.html
    https://www.vogella.com/tutorials/AndroidBackgroundProcessing/article.html
 */

class MainActivity : AppCompatActivity() {

    val TAG = "OTA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // (1) service

        val btnStart = findViewById<Button>(R.id.btn_start)
        val btnStop = findViewById<Button>(R.id.btn_stop)
        val btnStatus = findViewById<Button>(R.id.btn_status)

        btnStart.setOnClickListener {
            // TODO handle duplicate start
            Intent(this, MyService::class.java).also { startService(it) }
        }
        btnStop.setOnClickListener {
            Intent(this, MyService::class.java).also { stopService(it) }
        }
        btnStatus.setOnClickListener {
            val status = isMyServiceRunning(MyService::class.java)
            Log.d(TAG, status.toString())
        }

        // (2) intent service

        val btnIsStart = findViewById<Button>(R.id.btn_is_start)
        val btnIsStop = findViewById<Button>(R.id.btn_is_stop)
        val btnIsStatus = findViewById<Button>(R.id.btn_is_status)

        btnIsStart.setOnClickListener {
            // TODO prevent duplicate start
            Intent(this, MyIntentService::class.java).also { startService(it) }
        }
        btnIsStop.setOnClickListener {
            Intent(this, MyIntentService::class.java).also { stopService(it) }
        }
        btnIsStatus.setOnClickListener {
            val status = isMyServiceRunning(MyIntentService::class.java)
            Log.d(TAG, status.toString())
        }

        // (3) bound service

        val MSG_SAY_HELLO = 1

        /** Messenger for communicating with the service.  */
        var mService: Messenger? = null

        /** Flag indicating whether we have called bind on the service.  */
        var bound: Boolean = false

        /**
         * Class for interacting with the main interface of the service.
         */
        val mConnection = object : ServiceConnection {

            override fun onServiceConnected(className: ComponentName, service: IBinder) {
                // This is called when the connection with the service has been
                // established, giving us the object we can use to
                // interact with the service.  We are communicating with the
                // service using a Messenger, so here we get a client-side
                // representation of that from the raw IBinder object.
                mService = Messenger(service)
                bound = true
            }

            override fun onServiceDisconnected(className: ComponentName) {
                // This is called when the connection with the service has been
                // unexpectedly disconnected -- that is, its process crashed.
                mService = null
                bound = false
            }
        }

        fun sayHello() {
            if (!bound) return
            // Create and send a message to the service, using a supported 'what' value
            val msg: Message = Message.obtain(null, MSG_SAY_HELLO, 0, 0)
            try {
                mService?.send(msg)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }

        val btnBStart = findViewById<Button>(R.id.btn_b_start)
        val btnBStop = findViewById<Button>(R.id.btn_b_stop)
        val btnBHi = findViewById<Button>(R.id.btn_b_hi)
        val btnBStatus = findViewById<Button>(R.id.btn_b_status)

        btnBStart.setOnClickListener {
            // Bind to the service
            Intent(this, MessengerService::class.java).also { intent ->
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
            }
        }
        btnBStop.setOnClickListener {
            if (bound) {
                unbindService(mConnection)
                bound = false
            }
        }
        btnBHi.setOnClickListener {
            sayHello()
        }
        btnBStatus.setOnClickListener {
            val status = isMyServiceRunning(MessengerService::class.java)
            Log.d(TAG, status.toString())
        }

    }

    @Suppress("DEPRECATION")
    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}
