package com.example.livetolive

import android.animation.ObjectAnimator
import android.widget.ProgressBar

class barAnimation {
    fun animateProgress(bar: ProgressBar, from: Int, to: Int) {
        bar.progress = from

        ObjectAnimator.ofInt(bar, "progress", to).apply {
            duration = 1200
            interpolator = android.view.animation.DecelerateInterpolator()
            start()
        }
    }
}