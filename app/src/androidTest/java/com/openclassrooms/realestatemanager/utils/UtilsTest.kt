package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.net.wifi.WifiManager
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.openclassrooms.realestatemanager.utils.Utils.isConnectedToNetwork
import org.junit.Test


class UtilsIntegrationTest {


    @Test
    fun whenWifiEnableReturnsTrue() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.isWifiEnabled

        assertThat(context.isConnectedToNetwork()).isTrue()
    }
}