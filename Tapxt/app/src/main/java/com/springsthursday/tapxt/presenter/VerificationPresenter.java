package com.springsthursday.tapxt.presenter;

import android.content.Context;
import android.databinding.ObservableField;
import android.view.View;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.rx2.Rx2Apollo;
import com.springsthursday.tapxt.ConfirmSmsVerificationMutation;
import com.springsthursday.tapxt.GetUserProfileQuery;
import com.springsthursday.tapxt.item.FollowItem;
import com.springsthursday.tapxt.repository.UserInfo;
import com.springsthursday.tapxt.validation.Validation;
import com.springsthursday.tapxt.constract.VerificationContract;
import com.springsthursday.tapxt.database.DatabaseManager;
import com.springsthursday.tapxt.handler.UserInfoHandler;
import com.springsthursday.tapxt.item.UserInfoItem;
import com.springsthursday.tapxt.util.ApolloClientObject;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VerificationPresenter {

    private VerificationContract.View activity;
    private CompositeDisposable disposable;
    private String phoneNumber;

    public ObservableField<String> verificationNumber;

    public VerificationPresenter(VerificationContract.View view, String phoneNumber)
    {
        activity = view;
        this.phoneNumber = phoneNumber;

        verificationNumber = new ObservableField<>("");
    }

    public void ConfirmVerificationNumber(View view) {

        activity.hideKeybord();

        if(Validation.CheckVerificationNumber(verificationNumber.get()) == false)
        {
            activity.showInputVerificationNumberMessage();
            return;
        }

        this.confirmVerificationNuumber(verificationNumber.get(), phoneNumber);
    }

    public void confirmVerificationNuumber(String verificationNumber, final String phoneNumber)
    {
        activity.showProgressDialog("인증번호 확인 중 ...");

        ApolloClient apolloClienmt = ApolloClientObject.getApolloClient();

        ConfirmSmsVerificationMutation mutation = ConfirmSmsVerificationMutation.builder().secret(verificationNumber).phoneNumber(phoneNumber).build();
        ApolloCall<ConfirmSmsVerificationMutation.Data> apolloCall1 = apolloClienmt.mutate(mutation);
        Observable<Response<ConfirmSmsVerificationMutation.Data>> observable = Rx2Apollo.from(apolloCall1);

        disposable = new CompositeDisposable();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ConfirmSmsVerificationMutation.Data>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }
                    @Override
                    public void onNext(Response<ConfirmSmsVerificationMutation.Data> dataResponse) {
                        activity.hideProgressDialog();

                        if(dataResponse == null)
                        {
                            activity.showExceptionErrorMessage();
                            return;
                        }
                        if(dataResponse.errors().size() > 0)
                        {
                            activity.showResponseServerErrorMessage(dataResponse.errors().get(0).toString());
                            return;
                        }

                        successVerification(dataResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activity.hideProgressDialog();
                    }

                    @Override
                    public void onComplete() {}
                });
    }

    public void successVerification(Response<ConfirmSmsVerificationMutation.Data> dataResponse)
    {
        Context context = activity.getContext();

        if(dataResponse.data() == null)
        {
            return;
        }

        String token  = dataResponse.data().confirmSMSVerification();

        if(DatabaseManager.getInstance(context).isExistPhoneNumber(phoneNumber))
            DatabaseManager.getInstance(context).updateToken(phoneNumber, token);
        else
            DatabaseManager.getInstance(context).insertUserRecord(phoneNumber, token);

        UserInfoHandler.getInstance().setNumberNToken(phoneNumber, token);

        ApolloClient apolloClienmt = ApolloClientObject.getApolloClient();

        GetUserProfileQuery query = GetUserProfileQuery.builder().build();
        ApolloCall<GetUserProfileQuery.Data> apolloCall1 = apolloClienmt.query(query);
        Observable<Response<GetUserProfileQuery.Data>> observable = Rx2Apollo.from(apolloCall1);

        disposable = new CompositeDisposable();
        activity.showProgressDialog("사용자 정보 가져오는 중..");

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<GetUserProfileQuery.Data>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(Response<GetUserProfileQuery.Data> dataResponse) {

                        activity.hideProgressDialog();

                        if (dataResponse.data() == null) {
                            return;
                        }

                        UserInfoHandler.getInstance().initilizeUserInfo(dataResponse);
                        activity.MoveMainContentActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void dispose()
    {
        if(disposable != null && disposable.isDisposed() == false)
            disposable.dispose();
    }
}
