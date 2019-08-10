package fuel.hunter.view.notification

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import fuel.hunter.R

class NotificationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        inflate(context, R.layout.layout_notification, this)

        findViewById<View>(R.id.cancelIcon).setOnClickListener { dismiss() }
    }

    private fun dismiss() {
        ValueAnimator
            .ofInt(height, 0)
            .apply {
                duration = 300
                interpolator = AccelerateInterpolator()

                addUpdateListener {
                    layoutParams = layoutParams.apply {
                        height = it.animatedValue as Int
                    }
                    requestLayout()
                }

                doOnEnd { visibility = GONE }
            }
            .start()
    }
}