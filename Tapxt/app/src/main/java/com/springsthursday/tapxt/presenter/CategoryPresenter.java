package com.springsthursday.tapxt.presenter;

import com.springsthursday.tapxt.constract.CategoryContract;

public class CategoryPresenter {

    public CategoryContract.View activity;

    public CategoryPresenter(CategoryContract.View view)
    {
        this.activity = view;
    }
}
