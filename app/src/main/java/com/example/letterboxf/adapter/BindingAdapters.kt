package com.example.letterboxf.dataBindingAdapters

import android.media.Image
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.example.letterboxf.utils.Constants.IMAGE_BASE
import com.example.letterboxf.R
import com.squareup.picasso.Picasso

@BindingAdapter("loadImageFromUrl")
fun ImageView.loadImageFromUrl(url : String?){
    url.let {
        Picasso.get().load(IMAGE_BASE + it).into(this)
    }
}

@BindingAdapter("loadAvatarFromUrl")
fun ImageView.loadAvatarFromUrl(url : String?){
    if (url != null){
        val newUrl = url.slice(1 until url.length)
        Picasso.get().load(newUrl).error(R.drawable.ic_avatar).into(this)
    }
}

@BindingAdapter("popularity")
fun TextView.popularity(value : Double) {
    this.text = "Popularity: $value"
}
@BindingAdapter("textForRuntime")
fun TextView.textForRuntime(text : String?){
    if (text != null){
        this.text = text + " mins"
    }
}

@BindingAdapter("htmlText")
fun TextView.htmlToText(text:String?){
    if (text != null){
        this.text = Html.fromHtml(text,HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

}

@BindingAdapter("reviewOwner")
fun TextView.reviewOwner(name : String){
    text = Html.fromHtml("<font color = \"rgba(255, 255, 255, 0.5);\">Review by</font> <font color = \"#E9A6A6\">$name</font>",Html.FROM_HTML_MODE_COMPACT)
}

@BindingAdapter("rate")
fun RatingBar.rate(rating : Double){
    if (rating != 0.0){
        setRating(rating.toFloat())
    }else{
        visibility = View.GONE
    }
}

@BindingAdapter("visibleRate")
fun RatingBar.visibleRate(rating : Double){
    setRating(rating.toFloat())
}