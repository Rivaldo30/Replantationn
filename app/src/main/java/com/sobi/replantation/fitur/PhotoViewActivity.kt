package com.sobi.replantation.fitur

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.sobi.replantation.R
import com.squareup.picasso.Picasso

class PhotoViewActivity : AppCompatActivity() {
    companion object {
        const val paramImageUrl = "param_image_url"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_view)
        val photoUrl:String? = intent.getStringExtra(paramImageUrl)
        /*val photoUrl = if (url != null)
            "https://s3-ap-southeast-1.amazonaws.com/sobi-server/prod/image/${url}" else null*/
        val photoView = findViewById<ImageView>(R.id.photo_view)
        Picasso.get().load(photoUrl)
            .resize(200, 300)
            .onlyScaleDown()
            .placeholder(R.drawable.avatar)
            .error(R.drawable.avatar).into(photoView)
    }
}