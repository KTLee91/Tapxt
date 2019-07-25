package com.springsthursday.tapxt.constract;

import com.springsthursday.tapxt.item.StoryItem;

public interface HomeContract {
    interface View{
        void startAutoScroll();
        void openContentCover(String id);
        void addIndicator();
    }
}
