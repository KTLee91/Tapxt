package com.springsthursday.tapxt.constract;

public interface ContentCoverContract {
    interface View{
        void openContentActivity(String contentId);
        void openCommentActivity(String episodeId);
    }
}
