package com.example.testpro.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.view.*
import android.view.animation.AccelerateInterpolator
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.example.testpro.utils.Dimens.dpToPx
import com.example.testpro.base.BaseActivity
import com.example.testpro.databinding.FloatingViewBinding
import com.example.testpro.utils.Dimens
import com.tencent.mmkv.MMKV
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

interface MatchingFloatViewDelegate {

    fun matchingFloatViewDelegateDidPressed()

    fun matchingFloatViewDelegateDidDismiss()

    fun connectionFloatViewDelegateDidDismiss(matchUniqueId:String)
}

class MatchingFloatView @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    RelativeLayout(mContext, attrs, defStyleAttr) {

    enum class Mode {
        NORMAL, CONNECTING, WAITING
    }

    companion object {

        @Suppress("unused")
        @JvmStatic
        private val TAG = "FloatingView"

        @JvmStatic
        val keyOfFirstShowTipsInDay = "keyOfFirstShowTipsInDay"
    }

    var mode = Mode.NORMAL
    var matchUniqueId:String = ""
    lateinit var delegate: MatchingFloatViewDelegate

    private var floatBallParamsX = -1

    private var floatBallParamsY = -1

    private var floatBallParams: WindowManager.LayoutParams = WindowManager.LayoutParams()

    private var windowManager: WindowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    private var inputStartX = 0

    private var inputStartY = 0

    private var viewStartX = 0

    private var viewStartY = 0

    private var inMovingX = 0

    private var inMovingY = 0

    private val screenHeight: Int = Dimens.screenHeight

    private val screenWidth: Int = Dimens.screenWidth

    private val dp160: Int = dpToPx(160)

    private val dp48: Int = dpToPx(48)

    private var valueAnimator: ValueAnimator? = null

    private var moveVertical = false

    private val slop: Int = 3

    private var isDrag = false

    var isShow = false

    private val binding: FloatingViewBinding =
        FloatingViewBinding.inflate(LayoutInflater.from(mContext), this, true)


    @Suppress("unused")
    private val isLeftSide: Boolean
        get() = x == 0f

    @Suppress("unused")
    private val isRightSide: Boolean
        get() = x == (screenWidth - width).toFloat()


    init {
        floatBallParams.flags = floatBallParams.flags or
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

        floatBallParams.dimAmount = 0.2f
        floatBallParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        floatBallParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        @SuppressLint("RtlHardcoded")
        floatBallParams.gravity = Gravity.LEFT or Gravity.TOP
        floatBallParams.format = PixelFormat.RGBA_8888
        /* 设置整个窗口的透明度 */
        floatBallParams.alpha = 1.0f
        /* 显示悬浮球在屏幕左上角 */
        floatBallParams.x = 0
        floatBallParams.y = Dimens.screenHeight / 2
        binding.dismissImageView.setOnClickListener {
            if (mode == Mode.CONNECTING) {
                //当点击的时候要做一下操作
                delegate?.connectionFloatViewDelegateDidDismiss(matchUniqueId)
            }
        }
        setOnClickListener {
            delegate?.matchingFloatViewDelegateDidPressed()
        }
    }

    private fun dismissImageViewPressed() {
        Toast.makeText(mContext, "关闭",Toast.LENGTH_LONG).show()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (null != valueAnimator && valueAnimator!!.isRunning) {
                    valueAnimator!!.cancel()
                }
                isPressed = true
                isDrag = false
                inputStartX = event.rawX.toInt()
                inputStartY = event.rawY.toInt()
                viewStartX = floatBallParams.x
                viewStartY = floatBallParams.y
            }
            MotionEvent.ACTION_MOVE -> {
                inMovingX = event.rawX.toInt()
                inMovingY = event.rawY.toInt()
                val moveX = viewStartX + inMovingX - inputStartX
                val moveY = viewStartY + inMovingY - inputStartY
                if (screenHeight <= 0 || screenWidth <= 0) {
                    isDrag = false
                    return isDrag || super.onTouchEvent(event)
                }
                if (abs(inMovingX - inputStartX) > slop && abs(inMovingY - inputStartY) > slop) {
                    isDrag = true
                    floatBallParams.x = moveX
                    floatBallParams.y = moveY
                    updateWindowManager()
                } else {
                    isDrag = false
                }
            }
            MotionEvent.ACTION_UP -> {
                if (isDrag) {
                    /* 恢复按压效果 */
                    isPressed = false
                }
                /* 吸附贴边计算和动画 */
                welt()
            }
            else -> {
            }
        }
        return isDrag || super.onTouchEvent(event)
    }

    /* 吸附贴边计算和动画 */
    private fun welt() {
        var movedX = floatBallParams.x
        var movedY = floatBallParams.y
        moveVertical = false
        if (floatBallParams.y < height && floatBallParams.x >= slop && floatBallParams.x <= screenWidth - width - slop) {
            movedY = 0
        } else if (floatBallParams.y > screenHeight - height * 2 && floatBallParams.x >= slop && floatBallParams.x <= screenWidth - width - slop) {
            movedY = screenHeight - height
        } else {
            moveVertical = true
            movedX = if (floatBallParams.x < screenWidth / 2 - width / 2) {
                0
            } else {
                screenWidth - width
            }
        }
        val duration: Int
        if (moveVertical) {
            valueAnimator = ValueAnimator.ofInt(floatBallParams.x, movedX)
            duration = movedX - floatBallParams.x
        } else {
            valueAnimator = ValueAnimator.ofInt(floatBallParams.y, movedY)
            duration = movedY - floatBallParams.y
        }
        valueAnimator?.duration = abs(duration).toLong()
        valueAnimator?.addUpdateListener { animation ->
            val level = animation.animatedValue as Int
            if (moveVertical) {
                floatBallParams.x = level
            } else {
                floatBallParams.y = level
            }
            updateWindowManager()
        }
        valueAnimator?.interpolator = AccelerateInterpolator()
        valueAnimator?.start()
    }

    override fun onDetachedFromWindow() {
        if (null != valueAnimator && valueAnimator!!.isRunning) {
            valueAnimator!!.cancel()
        }
        /* 进入下个页面的时候贴边动画暂停，下个页面attached时候会继续动画， 你手速快的话还能在中途接住球继续拖动 */
        super.onDetachedFromWindow()
    }

    fun showTip(activity: BaseActivity? = null) {
        /* 显示气泡 */
        if (isShow) {
            val currentDate = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).format(Date(System.currentTimeMillis()))
            val oldDate = MMKV.mmkvWithID(TAG)?.getString(keyOfFirstShowTipsInDay, "")
            if (oldDate.isNullOrBlank() || oldDate.contentEquals(currentDate).not()) {
                MMKV.mmkvWithID(TAG)?.putString(keyOfFirstShowTipsInDay, currentDate)
            }
        }
    }

    /* 显示悬浮 */
    fun show(activity: BaseActivity? = null) {
        showTip(activity)

        if (isShow) {
            return
        }
        isShow = true
        if (floatBallParamsX == -1 || floatBallParamsY == -1) {
            /* 首次打开时，初始化的位置 */
            floatBallParams.x = dp48
            floatBallParams.y = screenHeight - dp160 - dp48
            floatBallParamsX = floatBallParams.x
            floatBallParamsY = floatBallParams.y
        } else {
            floatBallParams.x = floatBallParamsX
            floatBallParams.y = floatBallParamsY
        }

        windowManager.addView(this, floatBallParams)
        /* 吸附贴边计算和动画 */
        welt()
    }

    /* 移除该view */
    @Suppress("unused")
    fun hide() {
        if (isShow) {
            isShow = false
            windowManager.removeViewImmediate(this)
        }
    }

    /* 更新位置，并保存到手机内存 */
    private fun updateWindowManager() {
        windowManager.updateViewLayout(this, floatBallParams)
        floatBallParamsX = floatBallParams.x
        floatBallParamsY = floatBallParams.y
    }


    fun bind() {
        this.mode = mode
        this.matchUniqueId = matchUniqueId
        binding.rotateAnimationView.bind(mode)
        binding.currentStateTextView.text = "等待中"
    }
}