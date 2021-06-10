package com.example.testpro.utils

import android.content.res.Resources
import android.graphics.Rect

object Dimens {

    private var safeAreaCalculated: Boolean = false
    var safeArea: Rect = Rect()
        set(rect) {
            field = rect
            if (safeAreaCalculated.not()) {
                safeAreaCalculated = true
            }
        }

    /* Make sure following constants match dimens.xml */
    val marginXSmall = dpToPx(2)
    val marginSmall = dpToPx(5)
    val marginMedium = dpToPx(8)
    val marginLarge = dpToPx(12)
    val marginXLarge = dpToPx(24)
    val marginXXLarge = dpToPx(36)
    val marginXXXLarge = dpToPx(72)

    val fontSizeXXSmall = 8f
    val fontSizeXSmall = 10f
    val fontSizeSmall = 12f
    val fontSizeMedium = 14f
    val fontSizeLarge = 16f
    val fontSizeXLarge = 24f

    val purchaseableItemWidth = dpToPx(100)

    /* Minimum height for purchasable cell is 114dp */
    val purchaseableItemHeight = dpToPx(100)

    val smallAvatarSize = dpToPx(32)

    val mediumAvatarSize = dpToPx(48)

    val largeAvatarSize = dpToPx(64)

    val circleButtonDhefaultItemHeight = dpToPx(50)

    val purchaseableDiamondItemHeight = dpToPx(120)

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    val screenWidth: Int by lazy { Resources.getSystem().displayMetrics.widthPixels }
    val screenHeight: Int by lazy { Resources.getSystem().displayMetrics.heightPixels }

    const val displayPictureMaxLength: Int = 512
}