package com.springsthursday.tapxt.util;

import com.springsthursday.tapxt.Code.Code;
import com.springsthursday.tapxt.SeeEpisodeQuery;

public class ContentUtil {

    public static int getTextContentType(SeeEpisodeQuery.Content content, boolean isContextScene, String preName, String prePosition)
    {
        if(content.contentPosition().position().equals("center"))
        {
            return Code.ContentType.NARRATION_CONTENT;
        }
        else if(content.contentPosition().position().equals("left"))
        {
            if(isContextScene == false)
            {
                if(content.character().name().equals(preName) && !prePosition.equals("center"))
                    return Code.ContentType.LEFT_SERIES_CONTENT;
                else
                    return Code.ContentType.LEFT_FIRST_CONTENT;
            }
            return Code.ContentType.LEFT_FIRST_CONTENT;
        }
        else
        {
            if(isContextScene == false)
            {
                if(content.character().name().equals(preName) && !prePosition.equals("center"))
                    return Code.ContentType.RIGHT_SERIES_CONTENT;
                else
                    return Code.ContentType.RIGHT_FIRST_CONTENT;
            }
            return Code.ContentType.RIGHT_FIRST_CONTENT;
        }
    }

    public static int getImageContentType(SeeEpisodeQuery.Content content, boolean isContextScene, String preName, String prePosition)
    {
        if(content.contentPosition().position().equals("center"))
        {
            return Code.ContentType.CENTER_IMAGE;
        }
        else if(content.contentPosition().position().equals("left"))
        {
            if(isContextScene == false)
            {
                if(content.character().name().equals(preName) && !prePosition.equals("center"))
                    return Code.ContentType.LEFT_SERIES_IMAGE;
                else
                    return Code.ContentType.LEFT_IMAGE;
            }
            return Code.ContentType.LEFT_IMAGE;
        }
        else
        {
            if(isContextScene == false)
            {
                if(content.character().name().equals(preName) && !prePosition.equals("center"))
                    return Code.ContentType.RIGHT_SERIES_IMAGE;
                else
                    return Code.ContentType.RIGHT_IMAGE;
            }
            return Code.ContentType.RIGHT_IMAGE;
        }
    }
}
