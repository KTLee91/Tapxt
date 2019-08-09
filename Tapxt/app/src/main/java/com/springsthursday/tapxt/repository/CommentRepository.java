package com.springsthursday.tapxt.repository;

import com.apollographql.apollo.api.Response;
import com.springsthursday.tapxt.SeeCommentsQuery;
import com.springsthursday.tapxt.item.CommentItem;

import java.util.ArrayList;

public class CommentRepository {

    public static CommentRepository commentInfo = null;
    public ArrayList<CommentItem> commentItems=null;
    private Response<SeeCommentsQuery.Data> dataResponse;

    //MovieDatabaseManager 싱글톤 패턴으로 구현
    public static CommentRepository getInstance()
    {
        if(commentInfo == null)
        {
            commentInfo = new CommentRepository();
        }

        return commentInfo;
    }

    public ArrayList<CommentItem> getCommentList(Response<SeeCommentsQuery.Data> dataResponse)
    {
        this.dataResponse = dataResponse;
        ArrayList<CommentItem> commentItems = new ArrayList<CommentItem>();

        for(int i=0; i < dataResponse.data().seeEpisode().comments().size(); i++)
        {
            SeeCommentsQuery.Comment comment = dataResponse.data().seeEpisode().comments().get(i);

            CommentItem item = new CommentItem();

            item.setText(comment.text());
            item.setId(comment.id());
            item.setUserID(comment.user().id());
            item.setUserNickName(comment.user().nickname());
            item.setAvatar(comment.user().avatar());
            item.setMe(comment.isSelf());
            item.clapsCount.set(comment.clapsCount().toString());

            for(int j=0; j<comment.claps().size(); j++)
            {
                SeeCommentsQuery.Clap clap = comment.claps().get(j);
                item.addClaps(clap.user().nickname());
            }

            commentItems.add(item);
        }
        return commentItems;
    }
}