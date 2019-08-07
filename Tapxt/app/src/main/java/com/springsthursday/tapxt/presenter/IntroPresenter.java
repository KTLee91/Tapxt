package com.springsthursday.tapxt.presenter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.ObservableField;
import android.view.View;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.rx2.Rx2Apollo;
import com.springsthursday.tapxt.GetUserProfileQuery;
import com.springsthursday.tapxt.constract.IntroContract;
import com.springsthursday.tapxt.constract.LoginContract;
import com.springsthursday.tapxt.database.DatabaseManager;
import com.springsthursday.tapxt.handler.UserInfoHandler;
import com.springsthursday.tapxt.item.FollowItem;
import com.springsthursday.tapxt.item.UserInfoItem;
import com.springsthursday.tapxt.repository.AppSettingIngo;
import com.springsthursday.tapxt.repository.UserInfo;
import com.springsthursday.tapxt.util.ApolloClientObject;
import com.springsthursday.tapxt.view.LoginActivity;
import com.springsthursday.tapxt.view.MainActivity;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class IntroPresenter {

    private Context context;
    public CompositeDisposable disposable;
    private IntroContract.View activity;

    public IntroPresenter(Context context, IntroContract.View activity) {
        this.context = context;
        this.activity = activity;
    }

    public void loadData() {
        Cursor cursor = DatabaseManager.getInstance(context).getVerificationUser();

        if (cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                UserInfoItem userInfoItem = new UserInfoItem();
                userInfoItem.setPhoneNumber(cursor.getString(1));
                userInfoItem.setToken(cursor.getString(2));
                UserInfo.getInstance().userInfoItem = userInfoItem;
            }

            getUserInfo();
        } else {
            activity.contextLoginActivity();
        }
    }

    public void getUserInfo() {
        ApolloClient apolloClienmt = ApolloClientObject.getApolloClient();

        GetUserProfileQuery query = GetUserProfileQuery.builder().build();
        ApolloCall<GetUserProfileQuery.Data> apolloCall1 = apolloClienmt.query(query);
        Observable<Response<GetUserProfileQuery.Data>> observable = Rx2Apollo.from(apolloCall1);

        disposable = new CompositeDisposable();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<GetUserProfileQuery.Data>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(Response<GetUserProfileQuery.Data> dataResponse) {

                        if (dataResponse.data() == null) {
                            return;
                        }

                        UserInfoHandler.getInstance().initilizeUserInfo(dataResponse);
                        activity.contextManinActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }
}
