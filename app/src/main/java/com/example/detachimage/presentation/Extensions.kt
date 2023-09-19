import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.detachimage.presentation.getApplication

fun getAppString(@StringRes stringId: Int, context: Context? = getApplication()): String {
    return context?.getString(stringId) ?: ""
}

fun getAppString(
    @StringRes resId: Int,
    vararg formatArgs: Any?,
    context: Context? = getApplication()
): String {
    return context?.getString(resId, *formatArgs) ?: ""
}

fun getAppFont(@FontRes fontId: Int, context: Context? = getApplication()): Typeface? {
    context?.let {
        return ResourcesCompat.getFont(context, fontId)
    }
    return null
}

fun getAppDrawable(@DrawableRes drawableId: Int, context: Context? = getApplication()): Drawable? {
    if (context == null) {
        return null
    }
    return ContextCompat.getDrawable(context, drawableId)
}

fun getAppDimensionPixel(@DimenRes dimenId: Int, context: Context? = getApplication()) =
    context?.resources?.getDimensionPixelSize(dimenId) ?: -1

fun getAppDimension(@DimenRes dimenId: Int, context: Context? = getApplication()) =
    context?.resources?.getDimension(dimenId) ?: -1f

fun getAppColor(@ColorRes colorRes: Int, context: Context? = getApplication()) =
    context?.let { ContextCompat.getColor(it, colorRes) } ?: Color.TRANSPARENT
