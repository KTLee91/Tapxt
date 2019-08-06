package com.springsthursday.tapxt.presenter;

import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.rx2.Rx2Apollo;
import com.springsthursday.tapxt.EditUserMutation;
import com.springsthursday.tapxt.constract.ProfileUpdateContract;
import com.springsthursday.tapxt.repository.UserInfo;
import com.springsthursday.tapxt.util.ApolloClientObject;
import com.springsthursday.tapxt.validation.Validation;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProfileUpdatePresenter {

    public ObservableField<String> nickName = new ObservableField<>();;
    public ObservableField<String> imageUrl = new ObservableField<>();
    public ObservableField<String> editNickName = new ObservableField<>();
    private CompositeDisposable disposable;
    private ProfileUpdateContract.View activity;

    public ProfileUpdatePresenter(ProfileUpdateContract.View view)
    {
        this.activity = view;
    }

    public void setUserInfo(String nickName, String imageUrl)
    {
        this.nickName.set(nickName);
        this.imageUrl.set(imageUrl);
    }

    public void updateUserProfile()
    {

        if(Validation.isDuplicatedNickName(editNickName.get())) {
            activity.showDuplicatedNickNameMessage();
            return;
        }

        ApolloClient apolloClienmt = ApolloClientObject.getApolloClient();

        EditUserMutation mutation = EditUserMutation
                .builder()
                .avatar(imageUrl.get())
                .nickname(editNickName.get().isEmpty() ? nickName.get() : editNickName.get())
                .build();
        ApolloCall<EditUserMutation.Data> apolloCall1 = apolloClienmt.mutate(mutation);
        Observable<Response<EditUserMutation.Data>> observable = Rx2Apollo.from(apolloCall1);

        disposable = new CompositeDisposable();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<EditUserMutation.Data>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }
                    @Override
                    public void onNext(Response<EditUserMutation.Data> dataResponse) {
                        UserInfo.getInstance().userInfoItem.setNickName(editNickName.get().isEmpty() ? nickName.get() : editNickName.get());
                        UserInfo.getInstance().userInfoItem.setImageUrl(imageUrl.get());
                        activity.finishActivity(editNickName.get().isEmpty() ? nickName.get() : editNickName.get(), imageUrl.get());
                    }

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onComplete() {}
                });
    }

    public void clickComplete(View view)
    {

    }

    public void imageClickHandler(View view)
    {
        activity.openGallery();
    }
}
