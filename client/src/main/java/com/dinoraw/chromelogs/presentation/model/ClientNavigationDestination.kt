package com.dinoraw.chromelogs.presentation.model

sealed class ClientNavigationDestination(val route: String) {
    data object ClientScreen : ClientNavigationDestination("ClientScreen")
    data object OpenSettingDialog : ClientNavigationDestination("OpenSettingDialog")
    data object ClientConfigScreen : ClientNavigationDestination("ConfigScreen")
}