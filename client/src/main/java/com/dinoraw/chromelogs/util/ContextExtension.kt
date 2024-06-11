package com.dinoraw.chromelogs.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils
import com.dinoraw.chromelogs.presentation.service.GestureService

fun Context.openBrowser(
    browserPackageName: String = Const.CHROME_PACKAGE_NAME,
    browserDefaultPage: String = Const.CHROME_DEFAULT_PAGE
) {
    val intent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(browserDefaultPage))
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        .setPackage(browserPackageName)
    startActivity(intent)
}

fun Context.openAccessibilityServiceSetting() {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)
    startActivity(intent)
}

fun Context.accessibilityServiceSettingEnabled(): Boolean {
    val accessibilityEnabled = Settings.Secure.getInt(
        contentResolver,
        Settings.Secure.ACCESSIBILITY_ENABLED
    )

    val stringColonSplitter = TextUtils.SimpleStringSplitter(':')
    if (accessibilityEnabled == 1) {
        val enabledAccessibilityService: String = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        stringColonSplitter.setString(enabledAccessibilityService)
        while (stringColonSplitter.hasNext()) {
            val accessibilityService = stringColonSplitter.next()
            if (accessibilityService.equals("$packageName/${GestureService::class.java.canonicalName}", ignoreCase = true)) return true
        }
    }
    return false
}