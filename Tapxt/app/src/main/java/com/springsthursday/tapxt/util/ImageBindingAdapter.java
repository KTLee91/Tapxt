package com.springsthursday.tapxt.util;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.springsthursday.tapxt.R;

public class ImageBindingAdapter {
    @android.databinding.BindingAdapter("imageUrl")
    public static void loadImage(final ImageView imageView, final String imageUrl) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .apply(new RequestOptions().circleCrop()).into(imageView);
    }

    @BindingAdapter("imageResourceBinding")
    public static void bindingImage(final ImageView imageView, final String imageUrl) {
        Glide.with(imageView.getContext())
                .load(imageUrl).into(imageView);
    }

    @BindingAdapter("contextImage")
    public static void contextImage(final ImageView imageView, final String editComment) {
        if(editComment.isEmpty())
            imageView.setImageResource(R.drawable.ic_new_comment_white);
        else
            imageView.setImageResource(R.drawable.ic_new_comment_darkerviolet);
    }
}
