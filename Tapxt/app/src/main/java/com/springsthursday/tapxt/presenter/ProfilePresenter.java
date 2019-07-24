package com.springsthursday.tapxt.presenter;

import android.databinding.ObservableField;
import android.view.View;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.rx2.Rx2Apollo;
import com.springsthursday.tapxt.GetUserProfileQuery;
import com.springsthursday.tapxt.constract.ProfileContract;
import com.springsthursday.tapxt.database.DatabaseManager;
import com.springsthursday.tapxt.repository.UserInfo;
import com.springsthursday.tapxt.util.ApolloClientObject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProfilePresenter {
    private ProfileContract.View activity;
    private CompositeDisposable disposable;

    public ObservableField<String> nickName = new ObservableField<>();;
    public ObservableField<Integer> progressBarVisibility= new ObservableField<>();
    public ObservableField<String> imageUrl = new ObservableField<>();

    public ProfilePresenter(ProfileContract.View view)
    {
       progressBarVisibility.set(View.GONE);
        this.activity = view;
    }

    public void loadProfile()
    {
        progressBarVisibility.set(View.VISIBLE);
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
                        if(dataResponse.data() == null) {
                            progressBarVisibility.set(View.GONE);
                            return;
                        }

                        progressBarVisibility.set(View.GONE);

                        UserInfo.getInstance().userInfoItem.setNickName(dataResponse.data().me().nickname());
                        UserInfo.getInstance().userInfoItem.setImageUrl(dataResponse.data().me().avatar());

                        nickName.set(UserInfo.getInstance().userInfoItem.getNickName());
                        imageUrl.set(UserInfo.getInstance().userInfoItem.getImageUrl());
                    }

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onComplete() {}
                });
    }

    public void setUserInfo(String nickName, String imageUrl)
    {
        this.nickName.set(nickName);
        this.imageUrl.set(imageUrl);
    }

    public void logout(View view)
    {
        DatabaseManager.getInstance(activity.getContext()).updateNullToken(UserInfo.getInstance().userInfoItem.getPhoneNumber());
    }
}
