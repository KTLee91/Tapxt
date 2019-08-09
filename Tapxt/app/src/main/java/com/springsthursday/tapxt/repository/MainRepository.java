package com.springsthursday.tapxt.repository;

import com.apollographql.apollo.api.Response;
import com.springsthursday.tapxt.SeeAllStoryListQuery;
import com.springsthursday.tapxt.SeeFeedQuery;
import com.springsthursday.tapxt.item.BannerItem;
import com.springsthursday.tapxt.item.EpisodeItem;
import com.springsthursday.tapxt.item.StoryItem;

import java.util.ArrayList;

public class MainRepository {
    private static MainRepository mainRepository ;
    private ArrayList<ArrayList<StoryItem>> items = new ArrayList<>();

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

        for(int i=0; i<dataResponse.data().seeFeed().size(); i++)
        {
            SeeFeedQuery.SeeFeed feed = dataResponse.data().seeFeed().get(i);

            String tag = feed.text();

            ArrayList<StoryItem> storyList = new ArrayList<>();

            for(int k = 0; k < feed.stories().size(); k++)
            {
                StoryItem item = new StoryItem();
                SeeFeedQuery.Story story = feed.stories().get(k);

                item.setStoryid(story.id());
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

    public ArrayList<ArrayList<StoryItem>> loadAllStroyList(Response<SeeAllStoryListQuery.Data> dataResponse)
    {
        ArrayList<StoryItem> allStoryList = new ArrayList<>();

        for(int i =0 ; i<dataResponse.data().seeStories().size(); i++)
        {
            StoryItem item = new StoryItem();
            SeeAllStoryListQuery.SeeStory story = dataResponse.data().seeStories().get(i);

            item.setFeedTag("이달의 신작");
            item.setStoryid(story.id());
            item.setCover(story.cover());
            item.setTitle(story.title());

            allStoryList.add(item);
        }

        items.add(0, allStoryList);

        return items;
    }

    public ArrayList<ArrayList<StoryItem>> getItems()
    {
        return items;
    }

    public void dispose()
    {
        if(items != null)
            items.clear();
    }

}