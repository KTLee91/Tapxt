package com.springsthursday.tapxt.constract;

public interface ContentContract {

    interface View{
        void showEpisodeDialog();
        void openNextBtn(String nextEpisodeID);
        void openEpisodeList();
        void openCommentList();
        void closeDialog();
    }
}
