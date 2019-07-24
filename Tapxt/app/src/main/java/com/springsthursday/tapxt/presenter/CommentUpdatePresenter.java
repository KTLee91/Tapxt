package com.springsthursday.tapxt.presenter;

import android.content.Context;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.rx2.Rx2Apollo;
import com.springsthursday.tapxt.EditCommentMutation;
import com.springsthursday.tapxt.constract.CommentUpdateContract;
import com.springsthursday.tapxt.item.CommentItem;
import com.springsthursday.tapxt.util.ApolloClientObject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CommentUpdatePresenter {

    public ObservableField<String> editComment;
    public ObservableField<Integer> loaderVisibility;

    private Context context;
    private CommentItem item;
    private CompositeDisposable disposable;
    private CommentUpdateContract.View activity;
    private LottieAnimationView lottie;

    public CommentUpdatePresenter(CommentUpdateContract.View activity ,Context context, CommentItem item)
    {
        this.context = context;
        this.item = item;
        this.activity = activity;

        editComment = new ObservableField<>(item.getText());
        loaderVisibility = new ObservableField<>(View.GONE);
    }

    public void setLottie(LottieAnimationView lottie)
    {
        this.lottie = lottie;
    }

    public void editComment()
    {
        loaderVisibility.set(View.VISIBLE);
        lottie.playAnimation();

        ApolloClient apolloClienmt = ApolloClientObject.getApolloClient();

        EditCommentMutation mutation = EditCommentMutation
                .builder()
                .id(item.getId())
                .text(editComment.get())
                .build();
        ApolloCall<EditCommentMutation.Data> apolloCall1 = apolloClienmt.mutate(mutation);
        Observable<Response<EditCommentMutation.Data>> observable = Rx2Apollo.from(apolloCall1);

        disposable = new CompositeDisposable();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<EditCommentMutation.Data>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }
                    @Override
                    public void onNext(Response<EditCommentMutation.Data> dataResponse) {
                        if(dataResponse == null) {
                            Toast.makeText(context, "예기치 못한 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(dataResponse.errors().size() > 0) {
                            Toast.makeText(context, dataResponse.errors().get(0).toString(), Toast.LENGTH_LONG).show();
                            return;
                        }

                        loaderVisibility.set(View.GONE);
                        activity.finishActivity(editComment.get(), item.getId());

                    }

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onComplete() {}
                });
    }
}
