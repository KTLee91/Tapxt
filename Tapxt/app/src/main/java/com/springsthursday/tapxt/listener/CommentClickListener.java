package com.springsthursday.tapxt.listener;

import com.airbnb.lottie.LottieAnimationView;
import com.springsthursday.tapxt.item.CommentItem;

public interface CommentClickListener {
     void onUpdateTextClickListener(CommentItem item);
    void onDeleteTextClickListener(CommentItem item);
    void onLottieClickListener(CommentItem item, LottieAnimationView lottie);

}
