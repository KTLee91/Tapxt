package com.springsthursday.tapxt.constract;

import com.springsthursday.tapxt.item.CommentItem;

public interface CommentContract {
    interface View{
        void openCommentUpdateActivity(CommentItem item);
    }
}
