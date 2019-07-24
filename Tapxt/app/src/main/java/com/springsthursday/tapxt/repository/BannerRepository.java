package com.springsthursday.tapxt.repository;

import com.apollographql.apollo.api.Response;
import com.springsthursday.tapxt.InqueryBannersQuery;
import com.springsthursday.tapxt.item.BannerItem;
import com.springsthursday.tapxt.item.EpisodeItem;
import com.springsthursday.tapxt.item.StoryItem;

import java.util.ArrayList;

public class BannerRepository {

    private static ArrayList<BannerItem> bannerItems = new ArrayList<>();
    public static BannerRepository bannerRepository = new BannerRepository();

    public static BannerRepository getInstance()
    {
        if(bannerRepository == null)
        {
            bannerRepository = new BannerRepository();
        }

        return bannerRepository;
    }

    public void initializeBannerItems(Response<InqueryBannersQuery.Data> dataResponse)
    {
        bannerItems = new ArrayList<>();

        for(int i=0; i<dataResponse.data().seeBanners().size(); i++)
        {
            BannerItem bannerItem = new BannerItem();
            InqueryBannersQuery.SeeBanner banner = dataResponse.data().seeBanners().get(i);

            bannerItem.setId(banner.id());
            bannerItem.setTitle(banner.title());
            bannerItem.setDescription(banner.description());
            bannerItem.setUrl(banner.url());
            bannerItem.setThumb(banner.thumb());

            bannerItems.add(bannerItem);
        }
    }

    public ArrayList<BannerItem> getBannerItems()
    {
        return bannerItems;
    }
}