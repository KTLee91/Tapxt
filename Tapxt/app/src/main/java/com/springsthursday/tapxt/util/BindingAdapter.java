package com.springsthursday.tapxt.util;

import android.app.ActionBar;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.shape.RoundedCornerTreatment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.springsthursday.tapxt.BindingAdapter.CommentAdapter;
import com.springsthursday.tapxt.BindingAdapter.ContentAdapter;
import com.springsthursday.tapxt.BindingAdapter.ContentCoverAdapter;
import com.springsthursday.tapxt.BindingAdapter.MainAdapter;
import com.springsthursday.tapxt.BindingAdapter.MainBannerAdapter;
import com.springsthursday.tapxt.BindingAdapter.StoryListAdapter;
import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.item.BannerItem;
import com.springsthursday.tapxt.item.CommentItem;
import com.springsthursday.tapxt.item.StoryItem;

import java.util.ArrayList;

public class BindingAdapter {
    @android.databinding.BindingAdapter("bind_adapter")
    public static void setBindAdapter(RecyclerView recyclerView, ObservableField<ContentAdapter> adapter) {
        if (adapter != null)
            recyclerView.setAdapter(adapter.get());
    }


    @android.databinding.BindingAdapter("bind_background")
    public static void setBindBackground(ImageView imageView, String background) {

        Glide.with(imageView.getContext())
                .load(background)
                .apply(RequestOptions.placeholderOf(imageView.getDrawable()))
                .transition(DrawableTransitionOptions.withCrossFade(1000))
                .into(imageView);
    }

    @android.databinding.BindingAdapter("bind_background_switcher")
    public static void setBindBackgroundfotSwitch(ViewFlipper switcher, String background) {

        Glide.with(switcher.getContext())
                .load(background)
                .apply(RequestOptions.placeholderOf(switcher.getBackground()))
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        View view = LayoutInflater.from(switcher.getContext()).inflate(R.layout.view_all_background, switcher, false);
                        ImageView imageview = view.findViewById(R.id.background);
                        imageview.setBackground(resource);
                        switcher.addView(view);
                        switcher.showNext();
                    }
                });
    }

    @android.databinding.BindingAdapter("bind_background_round")
    public static void setBindBackgroundRound(ImageView imageView, String background) {
        int radius = (int) imageView.getContext().getResources().getDimension(R.dimen.corner_radius);

        MultiTransformation multiTransformation
                = new MultiTransformation(new RoundedCorners(radius));

        Glide.with(imageView.getContext())
                .load(background)
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .into(imageView);
    }

    @android.databinding.BindingAdapter("bindStoryItems")
    public static void setBindingStoryItems(final RecyclerView recyclerView, ObservableField item) {
        if (item != null) {
            final ContentCoverAdapter adapter = (ContentCoverAdapter) recyclerView.getAdapter();
            adapter.setItems((StoryItem) item.get());
        }
    }

    @android.databinding.BindingAdapter("bindContentCoverAdapter")
    public static void setBindContentCoverAdapter(RecyclerView recyclerView, ObservableField<ContentCoverAdapter> adapter) {
        if (adapter != null)
            recyclerView.setAdapter(adapter.get());
    }

    @android.databinding.BindingAdapter("bindCommentItems")
    public static void setBindingCommentItems(final RecyclerView recyclerView, ObservableField item) {
        if (item != null) {
            final CommentAdapter adapter = (CommentAdapter) recyclerView.getAdapter();
            adapter.setItems((ArrayList<CommentItem>) item.get());
        }
    }

    @android.databinding.BindingAdapter("bindCommentAdapter")
    public static void setCommentAdapter(RecyclerView recyclerView, ObservableField<CommentAdapter> adapter) {
        if (adapter != null)
            recyclerView.setAdapter(adapter.get());
    }

    @android.databinding.BindingAdapter("bindBannerItems")
    public static void setBindingManinBannerItems(final ViewPager viewPager, ObservableField item) {
        if (item != null && viewPager.getAdapter() != null) {
            final MainBannerAdapter adapter = (MainBannerAdapter) viewPager.getAdapter();
            adapter.setItems((ArrayList<BannerItem>) item.get());
        }
    }

    @android.databinding.BindingAdapter("bindMainBannerAdapter")
    public static void setMainBannerAdapter(ViewPager viewPager, ObservableField<MainBannerAdapter> adapter) {
        if (adapter != null)
            viewPager.setAdapter(adapter.get());
    }

    @android.databinding.BindingAdapter("bindMainItems")
    public static void setBindingMainItems(final RecyclerView recyclerView, ObservableField item) {
        if (item != null) {
            final MainAdapter adapter = (MainAdapter) recyclerView.getAdapter();
            adapter.setItems((ArrayList<ArrayList<StoryItem>>) item.get());
        }
    }

    @android.databinding.BindingAdapter("bindMainAdapter")
    public static void setBindingMainAdapter(RecyclerView recyclerView, ObservableField<MainAdapter> adapter) {
        if (adapter != null)
            recyclerView.setAdapter(adapter.get());
    }

    @android.databinding.BindingAdapter("bindStoryListItems")
    public static void setBindingStoryListAdapter(final RecyclerView recyclerView, ObservableField item) {
        if (item != null) {
            final StoryListAdapter adapter = (StoryListAdapter) recyclerView.getAdapter();
            adapter.setItems((ArrayList<StoryItem>)item.get());
        }
    }
    @android.databinding.BindingAdapter("bindStoryListAdapter")
    public static void setBindingStoryListItems(RecyclerView recyclerView, ObservableField<StoryListAdapter> adapter) {
        if (adapter != null)
            recyclerView.setAdapter(adapter.get());
    }
}
