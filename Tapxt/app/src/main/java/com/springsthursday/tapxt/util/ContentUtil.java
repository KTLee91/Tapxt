package com.springsthursday.tapxt.util;

import android.view.View;
import android.view.ViewGroup;

import com.springsthursday.tapxt.Code.Code;
import com.springsthursday.tapxt.SeeEpisodeMutation;

public class ContentUtil {

    public static int getTextContentType(SeeEpisodeMutation.Content content, boolean isContextScene, String preName, String prePosition)
    {
        if(content.contentPosition().position().equals("center"))
        {
            return Code.ContentType.NARRATION_CONTENT;
        }
        else if(content.contentPosition().position().equals("left"))
        {
            if(isContextScene == false)
            {
                if(content.character().name().equals(preName) && prePosition.equals("left"))
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
                if(content.character().name().equals(preName) && prePosition.equals("right"))
                    return Code.ContentType.RIGHT_SERIES_CONTENT;
                else
                    return Code.ContentType.RIGHT_FIRST_CONTENT;
            }
            return Code.ContentType.RIGHT_FIRST_CONTENT;
        }
    }

    public static int getImageContentType(SeeEpisodeMutation.Content content, boolean isContextScene, String preName, String prePosition)
    {
        if(content.contentPosition().position().equals("center"))
        {
            return Code.ContentType.CENTER_IMAGE;
        }
        else if(content.contentPosition().position().equals("left"))
        {
            if(isContextScene == false)
            {
                if(content.character().name().equals(preName) && prePosition.equals("left"))
                    return Code.ContentType.LEFT_SERIES_IMAGE;
                else
                    return Code.ContentType.LEFT_IMAGE;
            }
            return Code.ContentType.LEFT_IMAGE;
        }
        else if(content.contentPosition().position().equals("cover_background_image"))
        {
            return Code.ContentType.IMPACT_COVER_BACKGROUND;
        }
        else if(content.contentPosition().position().equals("bottom_background_image"))
        {
            return Code.ContentType.IMPACT_BOTTOM_BACKGROUND;
        }
        else
        {
            if(isContextScene == false)
            {
                if(content.character().name().equals(preName) &&prePosition.equals("right"))
                    return Code.ContentType.RIGHT_SERIES_IMAGE;
                else
                    return Code.ContentType.RIGHT_IMAGE;
            }
            return Code.ContentType.RIGHT_IMAGE;
        }
    }

    public static View findExactChild(View childView, float x, float y) {
        if (!(childView instanceof ViewGroup)) return childView;
        ViewGroup group = (ViewGroup) childView;
        final int count = group.getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View child = group.getChildAt(i);

            final float translationX = child.getTranslationX();
            final float translationY = child.getTranslationY();

            if (x >= child.getX() + translationX - 20 &&
                    x <= child.getX() + child.getWidth() + 20 + translationX &&
                    y >= child.getY() + translationY - 20 &&
                    y <= child.getY() + child.getHeight() + 20 + translationY) {
                return child;
            }
        }
        return null;
    }
}
