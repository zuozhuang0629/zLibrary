package com.wxzt.kfly

import android.app.Activity
import android.content.Intent
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.view.View

/**
 * This activity will launch when a USB accessory is attached and attempt to connect to the USB
 * accessory.
 */
class DJIConnectionControlActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(View(this))
        val usbIntent = intent
        if (usbIntent != null) {
            val action = usbIntent.action
            if (UsbManager.ACTION_USB_ACCESSORY_ATTACHED == action) {
                val attachedIntent = Intent()
                attachedIntent.action = ACCESSORY_ATTACHED
                sendBroadcast(attachedIntent)
            }
        }

        finish()
    }

    companion object {
        const val ACCESSORY_ATTACHED = "com.wxzt.kfly.ACCESSORY_ATTACHED"
    }
}