package com.springsthursday.tapxt.BindingAdapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.databinding.ViewpagerMainbannerBinding;
import com.springsthursday.tapxt.item.BannerItem;
import com.springsthursday.tapxt.item.StoryItem;

import java.util.ArrayList;

public class MainBannerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<BannerItem> bannerItemList;

    public MainBannerAdapter(Context context)
    {
        this.mContext = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ViewpagerMainbannerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.viewpager_mainbanner, container, false);

        Glide.with(mContext).load(bannerItemList.get(position).getThumb()).into(binding.imageView5);
        binding.setBannerItem(bannerItemList.get(position));

        container.addView(binding.getRoot());
        return binding.getRoot();
    }



    @Override
    public int getCount() {
        if(bannerItemList == null) return 0;

        return bannerItemList.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (View)o);
    }

    public void setItems(ArrayList<BannerItem> items)
    {
        this.bannerItemList = items;
        notifyDataSetChanged();
    }

    public BannerItem getCurrentItem(int position)
    {
        return bannerItemList.get(position);
    }
}