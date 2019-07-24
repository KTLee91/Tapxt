package com.springsthursday.tapxt.presenter;

import android.content.Context;
import android.databinding.ObservableField;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.rx2.Rx2Apollo;
import com.springsthursday.tapxt.CreateAccountMutation;
import com.springsthursday.tapxt.RequestSmsVerificationMutation;
import com.springsthursday.tapxt.validation.Validation;
import com.springsthursday.tapxt.constract.LoginContract;
import com.springsthursday.tapxt.util.ApolloClientObject;
import com.springsthursday.tapxt.view.LoginActivity;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.support.v4.content.ContextCompat.getSystemService;

public class LoginPresenter {

    private LoginContract.View activity;
    private CompositeDisposable disposable;
    public ObservableField<String> phoneNumber;
    private Context context;

    public LoginPresenter(LoginContract.View view, Context context)
    {
        this.activity = view;
        this.context = context;

        phoneNumber = new ObservableField<>("");
    }

    public void MoveVerificationAcitivity(View view)
    {
        activity.hideKeybord();
        if(Validation.CheckPhoneNumber(phoneNumber.get()) == false) {
            activity.showInvalidPhoneNumberMessage();
            return;
        }

        this.requestVerificationNumber(phoneNumber.get());
    }

    private void requestVerificationNumber(final String phoneNumber)
    {
        activity.showProgressDialog("요청 처리 중..");
        ApolloClient apolloClient = ApolloClientObject.getApolloClient();

        RequestSmsVerificationMutation mutation = RequestSmsVerificationMutation.builder().phoneNumber(phoneNumber).build();
        ApolloCall<RequestSmsVerificationMutation.Data> apolloCall1 = apolloClient.mutate(mutation);
        Observable<Response<RequestSmsVerificationMutation.Data>> observable = Rx2Apollo.from(apolloCall1);

        disposable = new CompositeDisposable();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<RequestSmsVerificationMutation.Data>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }
                    @Override
                    public void onNext(Response<RequestSmsVerificationMutation.Data> dataResponse) {
                        if(dataResponse == null)
                        {
                            activity.hideProgressDialog();
                            activity.showExceptionErrorMessage();
                            return;
                        }
                        if(dataResponse.errors().size() > 0)
                        {
                            activity.hideProgressDialog();
                            activity.showResponseServerErrorMessage(dataResponse.errors().get(0).toString());
                            return;
                        }

                        if(dataResponse.data().sendSMSVerification() == true) {
                            activity.hideProgressDialog();
                            activity.MoveVerificationActivity(phoneNumber);
                        }
                        else {
                            createAccount(phoneNumber);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        activity.hideProgressDialog();
                    }

                    @Override
                    public void onComplete() {}
                });
    }

    public void createAccount(final String phoneNumber)
    {
        ApolloClient apolloClient = ApolloClientObject.getApolloClient();

        CreateAccountMutation mutation = CreateAccountMutation.builder().phoneNumber(phoneNumber).build();
        ApolloCall<CreateAccountMutation.Data> apolloCall1 = apolloClient.mutate(mutation);
        Observable<Response<CreateAccountMutation.Data>> observable = Rx2Apollo.from(apolloCall1);

        disposable = new CompositeDisposable();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<CreateAccountMutation.Data>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }
                    @Override
                    public void onNext(Response<CreateAccountMutation.Data> dataResponse) {
                        if(dataResponse == null)
                        {
                            activity.hideProgressDialog();
                            activity.showExceptionErrorMessage();
                            return;
                        }
                        if(dataResponse.errors().size() > 0)
                        {
                            activity.hideProgressDialog();
                            activity.showResponseServerErrorMessage(dataResponse.errors().get(0).toString());
                            return;
                        }

                        if(dataResponse.data().createAccount() == true)
                            requestVerificationNumber(phoneNumber);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activity.hideProgressDialog();
                    }

                    @Override
                    public void onComplete() {}
                });
    }

    public void dispose()
    {
        if(disposable != null && disposable.isDisposed() == false)
            disposable.dispose();
    }
}
