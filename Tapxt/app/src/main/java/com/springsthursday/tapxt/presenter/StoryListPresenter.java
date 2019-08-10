package com.springsthursday.tapxt.presenter;
import android.content.Context;
import android.databinding.ObservableField;

import com.springsthursday.tapxt.BindingAdapter.StoryListAdapter;
import com.springsthursday.tapxt.Code.Code;
import com.springsthursday.tapxt.constract.ProfileContract;
import com.springsthursday.tapxt.item.StoryItem;
import com.springsthursday.tapxt.listener.StoryClickListener;
import com.springsthursday.tapxt.repository.UserInfo;

import java.util.ArrayList;

public class StoryListPresenter {

    private int type;
    private Context context;
    private ProfileContract.View activity;
    public ObservableField<StoryListAdapter> adapter;
    public ObservableField<ArrayList<StoryItem>> items;

    public StoryListPresenter(int type, Context context, com.springsthursday.tapxt.listener.StoryClickListener listener)
    {
        this.type = type;
        this.context = context;

        adapter = new ObservableField<>(new StoryListAdapter(listener));
        items = new ObservableField<>();
    }

    public void bindingData()
    {
        switch (type)
        {
            case Code.StoryListCategory.LIKED_STORYS:
                items.set(UserInfo.getInstance().userInfoItem.getLikeItems());
                break;
            case Code.StoryListCategory.Viewed_STROYS:
                items.set(UserInfo.getInstance().userInfoItem.getViewedItems());
                break;
        }
    }

}
