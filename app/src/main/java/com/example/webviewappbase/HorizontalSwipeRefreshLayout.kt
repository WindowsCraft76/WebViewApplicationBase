package com.example.webviewappbase

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.webkit.WebView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlin.math.abs

class HorizontalSwipeRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {

    private var touchX = 0f
    private var touchY = 0f
    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private var webView: WebView? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findWebView(this)
    }

    private fun findWebView(view: View) {
        if (view is WebView) {
            webView = view
        } else if (view is android.view.ViewGroup) {
            for (i in 0 until view.childCount) {
                findWebView(view.getChildAt(i))
                if (webView != null) break
            }
        }
    }

    override fun canChildScrollUp(): Boolean {
        webView?.let {
            return it.canScrollVertically(-1)
        }
        return super.canChildScrollUp()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                touchX = ev.x
                touchY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = abs(ev.x - touchX)
                val dy = abs(ev.y - touchY)
                if (dx > touchSlop && dx > dy) {
                    return false
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}
