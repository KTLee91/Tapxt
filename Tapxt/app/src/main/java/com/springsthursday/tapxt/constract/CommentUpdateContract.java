package com.springsthursday.tapxt.constract;

public interface CommentUpdateContract {
    interface View{
        void finishActivity(String comment, String commentID);
        void showProgressDialog(String message);
        void hideProgressDialog();
        void hideKeybord();
    }
}
