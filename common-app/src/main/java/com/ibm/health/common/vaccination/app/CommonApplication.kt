package com.ibm.health.common.vaccination.app

import android.app.Application
import android.webkit.WebView
import androidx.fragment.app.FragmentActivity
import com.ibm.health.common.android.utils.AndroidDependencies
import com.ibm.health.common.android.utils.androidDeps
import com.ibm.health.common.android.utils.isDebuggable
import com.ibm.health.common.http.HttpLogLevel
import com.ibm.health.common.http.httpConfig
import com.ibm.health.common.logging.Lumber
import com.ibm.health.common.navigation.android.*
import com.ibm.health.common.securityprovider.initSecurityProvider
import com.ibm.health.vaccination.sdk.android.dependencies.SdkDependencies
import com.ibm.health.vaccination.sdk.android.dependencies.sdkDeps

/** Common base application with some common functionality like setting up logging. */
public abstract class CommonApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // IMPORTANT: The security provider has to be initialized before anything else
        initSecurityProvider()

        if (isDebuggable) {
            Lumber.plantDebugTreeIfNeeded()
            httpConfig.enableLogging(HttpLogLevel.HEADERS)
            WebView.setWebContentsDebuggingEnabled(true)
        }

        navigationDeps = object : NavigationDependencies() {
            override val application = this@CommonApplication
            override val defaultScreenOrientation = Orientation.PORTRAIT
            override val animationConfig = DefaultNavigationAnimationConfig(250)
        }
        androidDeps = object : AndroidDependencies() {
            private val activityNavigator = ActivityNavigator()

            override val application: Application = this@CommonApplication

            override fun currentActivityOrNull(): FragmentActivity? =
                activityNavigator.getCurrentActivityOrNull() as? FragmentActivity
        }
        sdkDeps = object : SdkDependencies() {
            override val application: Application = this@CommonApplication

            // FIXME: Switch this based on the app variant
            override val productionMode: Boolean = false
        }
    }
}
