package com.springsthursday.tapxt.listener;

import android.widget.ToggleButton;

import com.airbnb.lottie.LottieAnimationView;
import com.springsthursday.tapxt.item.EpisodeItem;
import com.springsthursday.tapxt.item.StoryItem;

public interface Listener {

    interface OnItemClickListener{
        void onItemClick(EpisodeItem item);
        void onTextClick(int oederby);
        void onAlarmClick(ToggleButton lottie);
        void onFollowClick(ToggleButton lottie, StoryItem item);
    }
}
