package com.springsthursday.tapxt.repository;
import com.apollographql.apollo.api.Response;
import com.springsthursday.tapxt.Code.Code;

import com.springsthursday.tapxt.SeeEpisodeMutation;
import com.springsthursday.tapxt.item.ContentItem;
import com.springsthursday.tapxt.util.ContentUtil;

import java.util.ArrayList;

public class ContentRepostory {

    private static ContentRepostory contentRepostory = null;
    private ContentItem dummyContent;
    private ContentItem contextPreSceneTypeItem;
    public String episodeId;
    public String episodeTitle;
    public String storyTitle;

    public static ContentRepostory getInstance()
    {
        if(contentRepostory == null)
        {
            contentRepostory = new ContentRepostory();
        }

        return contentRepostory;
    }

    public ContentRepostory()
    {
        ContentItem dummyitem = new ContentItem();
        ContentItem contextSceneItem = new ContentItem();

        dummyitem.setContentSequence(999999);
        dummyitem.setContentType(Code.ContentType.DUMMY_CONTENT);

        contextSceneItem.setContentType(Code.ContentType.CONTEXT_PRE_SCENE);
        contextSceneItem.setContentSequence(-1);

        dummyContent = dummyitem;
        contextPreSceneTypeItem = contextSceneItem;
    }

    public ArrayList<ContentItem> LoadContentList(Response<SeeEpisodeMutation.Data> dataResponse)
    {
        ArrayList<ContentItem> contentList = new ArrayList<>();

        ContentItem item;
        int contentSequence = 0;
        String preName = "";
        String prePosition = "";

        //region Initialize Episode Values
        episodeTitle = dataResponse.data().viewingEpisode().title();
        storyTitle = dataResponse.data().viewingEpisode().story().title();
        //endregion

        for(int i = 0 ; i < dataResponse.data().viewingEpisode().scenes().size(); i++)
        {
            SeeEpisodeMutation.Scene scene = dataResponse.data().viewingEpisode().scenes().get(i);

            for(int k = 0; k < scene.contents().size(); k++)
            {
                SeeEpisodeMutation.Content content = scene.contents().get(k);

                item = new ContentItem();

                if(k==0)
                    item.setContextScene(true);
                else
                    item.setContextScene(false);

                item.setSceneID(scene.id());
                item.setSceneTitle(scene.title());
                item.setSceneSequence(scene.sequence());
                item.setSceneSound(scene.sceneProperty().sound());

                item.setAvatar(content.character().avatar());
                item.setName(content.character().name());
                item.setText(content.text());
                item.setUrl(content.url());

                item.setContentSequence(++contentSequence);
                item.setContentTypeForString(content.contentType().type());

                switch(content.contentType().type())
                {
                    case "text":
                        item.setContentType(ContentUtil.getTextContentType(content,item.getContextScene() ,preName, prePosition));
                        break;
                    case "image":
                        item.setContentType(ContentUtil.getImageContentType(content,item.getContextScene() ,preName, prePosition));
                        break;
                }

                item.setTextColor(content.contentProperty().textColor());
                item.setBoxColor(content.contentProperty().boxColor());
                item.setBold(content.contentProperty().bold());
                item.setItalic(content.contentProperty().italic());
                item.setBackground(content.url());
                item.setColor(content.color());

                preName = content.character().name();
                prePosition = content.contentPosition().position();

                contentList.add(item);
            }
        }
        return contentList;
    }

    public ContentItem getDummyContent() { return dummyContent;}

    public ContentItem getContextPreSceneType()
    {
        return contextPreSceneTypeItem;
    }
}
