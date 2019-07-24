package com.springsthursday.tapxt.repository;

import com.springsthursday.tapxt.item.StoryItem;

import java.util.ArrayList;

public class StoryRepository {

    public static StoryRepository storyRepository = null;
    public static StoryItem selectedStoryItem = new StoryItem();
    public static ArrayList<StoryItem> allStoryItem = new ArrayList<>();

    public static StoryRepository getInstance()
    {
        if(storyRepository == null)
        {
            storyRepository = new StoryRepository();
        }

        return storyRepository;
    }

    public void setSelectedStory(StoryItem item)
    {
        selectedStoryItem = item;
    }

    public StoryItem getSelectedStory() {return selectedStoryItem;}
}
