package com.dinoraw.chromelogs.presentation.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.graphics.Path
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import com.dinoraw.chromelogs.domain.model.GestureServiceResult
import com.dinoraw.chromelogs.presentation.model.GestureServiceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GestureService : AccessibilityService() {
    private var _serviceState: MutableStateFlow<GestureServiceState> = MutableStateFlow(GestureServiceState(openedApplication = "", isActive = false))
    val serviceState: StateFlow<GestureServiceState> get() = _serviceState.asStateFlow()

    private lateinit var windowManager: WindowManager
    companion object {
        var instance: GestureService? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        _serviceState.update {
            it.copy(openedApplication = "${event.packageName}")
        }
    }

    override fun onInterrupt() {

    }

    override fun onServiceConnected() {
        _serviceState.update { it.copy(isActive = true) }
        super.onServiceConnected()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        _serviceState.update { it.copy(isActive = false) }
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    // start and end coordinates in [0.1..0.9]
    fun onSwipe(
        heightPixels: Int = windowManager.currentWindowMetrics.bounds.height(),
        widthPixels: Int = windowManager.currentWindowMetrics.bounds.width(),
        startX: Float = 0.5f,
        endX: Float = 0.5f,
        startY: Float = 0.5f,
        endY: Float = 0.5f,
    ): GestureServiceResult {
        val startOfVerticalSwipe = heightPixels * startY
        val endOfVerticalSwipe = heightPixels * endY
        val startOfHorizontalSwipe = widthPixels * startX
        val endOfHorizontalSwipe = widthPixels * endX
        val gestureStartTime = 0L
        val gestureDuration = 500L

        val swipePath = Path().apply {
            moveTo(startOfHorizontalSwipe, startOfVerticalSwipe)
            lineTo(endOfHorizontalSwipe, endOfVerticalSwipe)
        }

        val gestureDescription = GestureDescription.Builder()
            .addStroke(
                GestureDescription.StrokeDescription(
                    swipePath,
                    gestureStartTime,
                    gestureDuration,
                    false
                )
            ).build()

        dispatchGesture(gestureDescription, null, null)

        return GestureServiceResult(
            startOfHorizontalSwipe = startOfHorizontalSwipe.toInt(),
            endOfHorizontalSwipe = endOfHorizontalSwipe.toInt(),
            startOfVerticalSwipe = startOfVerticalSwipe.toInt(),
            endOfVerticalSwipe = endOfVerticalSwipe.toInt(),
            maxHeight = heightPixels,
            maxWidth = widthPixels,
        )
    }
}


