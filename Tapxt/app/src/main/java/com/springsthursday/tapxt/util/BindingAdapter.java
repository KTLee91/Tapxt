package com.springsthursday.tapxt.util;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.support.design.shape.RoundedCornerTreatment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.springsthursday.tapxt.BindingAdapter.CommentAdapter;
import com.springsthursday.tapxt.BindingAdapter.ContentAdapter;
import com.springsthursday.tapxt.BindingAdapter.ContentCoverAdapter;
import com.springsthursday.tapxt.BindingAdapter.MainAdapter;
import com.springsthursday.tapxt.BindingAdapter.MainBannerAdapter;
import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.item.BannerItem;
import com.springsthursday.tapxt.item.CommentItem;
import com.springsthursday.tapxt.item.StoryItem;

import java.util.ArrayList;

public class BindingAdapter {
    @android.databinding.BindingAdapter("bind_adapter")
    public static void setBindAdapter(RecyclerView recyclerView, ObservableField<ContentAdapter> adapter)
    {
        if(adapter != null)
            recyclerView.setAdapter(adapter.get());
    }

    @android.databinding.BindingAdapter("bind_items")
    public static void setBindItems(final RecyclerView recyclerView, ObservableArrayList items)
    {
        if(items != null)
        {
            final ContentAdapter adapter = (ContentAdapter)recyclerView.getAdapter();
            adapter.setItems(items);
            recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    recyclerView.scrollToPosition(adapter.getItemCount() -1);
                    recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }

    @android.databinding.BindingAdapter("bind_background")
    public static void setBindBackground(ImageView imageView, String background)
    {
        Glide.with(imageView.getContext()).load(background).into(imageView);
    }

    @android.databinding.BindingAdapter("bind_background_round")
    public static void setBindBackgroundRound(ImageView imageView, String background)
    {
        int radius = (int)imageView.getContext().getResources().getDimension(R.dimen.corner_radius);

        MultiTransformation multiTransformation
                = new MultiTransformation( new RoundedCorners(radius));

        Glide.with(imageView.getContext())
                .load(background)
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .into(imageView);
    }

    @android.databinding.BindingAdapter("bindStoryItems")
    public static void setBindingStoryItems(final RecyclerView recyclerView, ObservableField item)
    {
        if(item != null)
        {
            final ContentCoverAdapter adapter = (ContentCoverAdapter) recyclerView.getAdapter();
            adapter.setItems((StoryItem)item.get());
        }
    }

    @android.databinding.BindingAdapter("bindContentCoverAdapter")
    public static void setBindContentCoverAdapter(RecyclerView recyclerView, ObservableField<ContentCoverAdapter> adapter)
    {
        if(adapter != null)
            recyclerView.setAdapter(adapter.get());
    }

    @android.databinding.BindingAdapter("bindCommentItems")
    public static void setBindingCommentItems(final RecyclerView recyclerView, ObservableField item)
    {
        if(item != null)
        {
            final CommentAdapter adapter = (CommentAdapter)recyclerView.getAdapter();
            adapter.setItems((ArrayList<CommentItem>)item.get());
        }
    }

    @android.databinding.BindingAdapter("bindCommentAdapter")
    public static void setCommentAdapter(RecyclerView recyclerView, ObservableField<CommentAdapter> adapter)
    {
        if(adapter != null)
            recyclerView.setAdapter(adapter.get());
    }

    @android.databinding.BindingAdapter("bindBannerItems")
    public static void setBindingManinBannerItems(final ViewPager viewPager, ObservableField item)
    {
        if(item != null && viewPager.getAdapter() != null)
        {
            final MainBannerAdapter adapter = (MainBannerAdapter)viewPager.getAdapter();
            adapter.setItems((ArrayList<BannerItem>)item.get());
        }
    }

    @android.databinding.BindingAdapter("bindMainBannerAdapter")
    public static void setMainBannerAdapter(ViewPager viewPager, ObservableField<MainBannerAdapter> adapter)
    {
        if(adapter != null)
            viewPager.setAdapter(adapter.get());
    }

    @android.databinding.BindingAdapter("bindMainItems")
    public static void setBindingMainItems(final RecyclerView recyclerView, ObservableField item)
    {
        if(item != null)
        {
            final MainAdapter adapter = (MainAdapter)recyclerView.getAdapter();
            adapter.setItems((ArrayList<ArrayList<StoryItem>>)item.get());
        }
    }

    @android.databinding.BindingAdapter("bindMainAdapter")
    public static void setBindingMainAdapter(RecyclerView recyclerView, ObservableField<MainAdapter> adapter)
    {
        if(adapter != null)
            recyclerView.setAdapter(adapter.get());
    }

}
