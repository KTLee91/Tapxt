package com.springsthursday.tapxt.util;

import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.view.View;
import android.widget.ImageView;

@BindingMethods({
        @BindingMethod(
                type =ImageClickListenerBindingAdapter.class,
                attribute = "android:onClickListener",
                method ="setOnClickListener"
        )
})

public class ImageClickListenerBindingAdapter {
    @BindingAdapter("android:onClickListener")
    public static void setOnClickListener(
            ImageView view, View.OnClickListener listener)
    {
        view.setOnClickListener(listener);
    }
}
