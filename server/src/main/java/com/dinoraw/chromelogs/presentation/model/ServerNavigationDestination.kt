package com.dinoraw.chromelogs.presentation.model

sealed class ServerNavigationDestination(val route: String) {
    data object ServerScreen : ServerNavigationDestination("ServerScreen")
    data object ServerConfigScreen : ServerNavigationDestination("ServerConfigScreen")
    data object ServerLogScreen : ServerNavigationDestination("ServerLogScreen")
}