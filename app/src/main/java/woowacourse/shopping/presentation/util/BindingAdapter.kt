package woowacourse.shopping.presentation.util

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import woowacourse.shopping.R
import java.text.NumberFormat
import java.util.Locale

@BindingAdapter("bindLoadImage")
fun ImageView.loadImage(imgUrl: String) {
    Glide.with(context)
        .load(imgUrl)
        .into(this)
}

@BindingAdapter("bindTextFormattedCurrency")
fun TextView.textFormattedCurrency(price: Long) {
    text = price.currency(context)
}

fun Long.currency(context: Context): String {
    return when (Locale.getDefault().country) {
        Locale.KOREA.country -> context.getString(R.string.price_format_kor, this)
        else -> NumberFormat.getCurrencyInstance(Locale.getDefault()).format(this)
    }
}