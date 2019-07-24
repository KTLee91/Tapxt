package com.springsthursday.tapxt.repository;

import com.apollographql.apollo.api.Response;
import com.springsthursday.tapxt.SeeFeedQuery;
import com.springsthursday.tapxt.item.BannerItem;
import com.springsthursday.tapxt.item.EpisodeItem;
import com.springsthursday.tapxt.item.StoryItem;

import java.util.ArrayList;

public class MainRepository {
    public static MainRepository mainRepository = new MainRepository();

    public static MainRepository getInstance()
    {
        if(mainRepository == null)
        {
            mainRepository = new MainRepository();
        }

        return mainRepository;
    }

    public ArrayList<ArrayList<StoryItem>> loadFeedItemList(Response<SeeFeedQuery.Data> dataResponse)
    {
        ArrayList<ArrayList<StoryItem>> items = new ArrayList();

        for(int i=0; i<dataResponse.data().seeFeed().size(); i++)
        {
            SeeFeedQuery.SeeFeed feed = dataResponse.data().seeFeed().get(i);

            String tag = feed.text();

            ArrayList<StoryItem> storyList = new ArrayList<>();

            for(int k = 0; k < feed.stories().size(); k++)
            {
                StoryItem item = new StoryItem();
                SeeFeedQuery.Story story = feed.stories().get(k);

                item.setFeedTag(tag);
                item.setTitle(story.title());
                item.setDescription(story.description());
                item.setStoryid(story.id());
                item.setCover(story.cover());

                storyList.add(item);
            }
            items.add(storyList);
        }

        return items;
    }
}