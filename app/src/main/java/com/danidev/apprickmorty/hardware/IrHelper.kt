package com.danidev.apprickmorty.hardware

import android.content.Context
import android.hardware.ConsumerIrManager
class IrHelper(context: Context) {
    private val ir = context.getSystemService(Context.CONSUMER_IR_SERVICE) as? ConsumerIrManager

    val disponible get() = ir?.hasIrEmitter() == true
    private val patron = intArrayOf(9000, 4500, 560, 560, 560, 1690, 560, 560, 560, 1690, 560)
    fun enviar(): Boolean {
        val m = ir?.takeIf { it.hasIrEmitter() } ?: return false
        return runCatching { m.transmit(38_000, patron); true }.getOrDefault(false)
    }
}
