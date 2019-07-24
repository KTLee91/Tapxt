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
import com.springsthursday.tapxt.BindingAdapter.CommentAdapter;
import com.springsthursday.tapxt.CreateCommentMutation;
import com.springsthursday.tapxt.DeleteCommentMutation;
import com.springsthursday.tapxt.ToggleClapMutation;
import com.springsthursday.tapxt.constract.CommentContract;
import com.springsthursday.tapxt.item.CommentItem;
import com.springsthursday.tapxt.listener.CommentClickListener;
import com.springsthursday.tapxt.repository.CommentRepository;
import com.springsthursday.tapxt.repository.UserInfo;
import com.springsthursday.tapxt.util.ApolloClientObject;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CommentPresenter {

    public ObservableField<CommentAdapter> adapter;
    public ObservableField<ArrayList<CommentItem>> items;
    public ObservableField<String> appbarTitle;
    public ObservableField<String> editComment;
    public ObservableField<Integer> loaderVisibility;

    private CompositeDisposable disposable;
    private String episodeID;
    private Context context;
    private boolean flagAddingComment = false;
    private LottieAnimationView lottie;
    private CommentContract.View activity;

    public CommentPresenter(CommentContract.View activity,Context context)
    {
        this.activity = activity;
        this.context = context;

        adapter = new ObservableField<>();
        items = new ObservableField<>();
        appbarTitle = new ObservableField<>();
        editComment = new ObservableField<>("");
        loaderVisibility = new ObservableField<>(View.GONE);

        adapter.set(new CommentAdapter(new ClickListener()));
        appbarTitle.set("댓글 (" + CommentRepository.getInstance().getCommentList().size() + ")");
    }

    public void loadData()
    {
        items.set(CommentRepository.getInstance().getCommentList());
    }

    public void setEpisodeID(String episdeID)
    {
        this.episodeID = episdeID;
    }

    public void setLoader(LottieAnimationView lottie)
    {
        this.lottie = lottie;
    }

    public void saveComment(View view)
    {
        if(flagAddingComment == true) return;
        if(editComment.get().isEmpty()) return;

        lottie.playAnimation();
        loaderVisibility.set(View.VISIBLE);

        flagAddingComment = true;

        ApolloClient apolloClienmt = ApolloClientObject.getApolloClient();

        CreateCommentMutation mutation =  CreateCommentMutation
                .builder()
                .episode(this.episodeID)
                .text(editComment.get())
                .build();
        ApolloCall<CreateCommentMutation.Data> apolloCall1 = apolloClienmt.mutate(mutation);
        Observable<Response<CreateCommentMutation.Data>> observable = Rx2Apollo.from(apolloCall1);

        disposable = new CompositeDisposable();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<CreateCommentMutation.Data>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }
                    @Override
                    public void onNext(Response<CreateCommentMutation.Data> dataResponse) {
                        if(dataResponse == null) {
                            Toast.makeText(context, "예기치 못한 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(dataResponse.errors().size() > 0) {
                            Toast.makeText(context, dataResponse.errors().get(0).toString(), Toast.LENGTH_LONG).show();
                            return;
                        }

                        CommentItem commentItem = new CommentItem();
                        commentItem.setAvatar(UserInfo.getInstance().userInfoItem.getImageUrl());
                        commentItem.setUserNickName(UserInfo.getInstance().userInfoItem.getNickName());
                        commentItem.setText(editComment.get());
                        commentItem.setMe(true);
                        commentItem.setId(dataResponse.data().createComment());

                        adapter.get().addItem(commentItem);

                        flagAddingComment = false;
                        loaderVisibility.set(View.GONE);
                        appbarTitle.set("댓글 (" + CommentRepository.getInstance().getCommentList().size() + ")");
                    }
                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onComplete() {}
                });
    }

    class ClickListener implements CommentClickListener
    {
        @Override
        public void onUpdateTextClickListener(final CommentItem item) {
            activity.openCommentUpdateActivity(item);
        }

        @Override
        public void onDeleteTextClickListener(final CommentItem item) {
            loaderVisibility.set(View.VISIBLE);
            lottie.playAnimation();

            ApolloClient apolloClienmt = ApolloClientObject.getApolloClient();

            DeleteCommentMutation mutation =  DeleteCommentMutation
                    .builder()
                    .id(item.getId())
                    .build();
            ApolloCall<DeleteCommentMutation.Data> apolloCall1 = apolloClienmt.mutate(mutation);
            Observable<Response<DeleteCommentMutation.Data>> observable = Rx2Apollo.from(apolloCall1);

            disposable = new CompositeDisposable();

            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<DeleteCommentMutation.Data>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }
                        @Override
                        public void onNext(Response<DeleteCommentMutation.Data> dataResponse) {
                            if(dataResponse == null) {
                                Toast.makeText(context, "예기치 못한 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                                return;
                            }
                            if(dataResponse.errors().size() > 0) {
                                Toast.makeText(context, dataResponse.errors().get(0).toString(), Toast.LENGTH_LONG).show();
                                return;
                            }
                            if(dataResponse.data().deleteComment() == true) {
                                loaderVisibility.set(View.GONE);
                                adapter.get().removeItem(item);
                                appbarTitle.set("댓글 (" + CommentRepository.getInstance().getCommentList().size() + ")");
                            }

                        }
                        @Override
                        public void onError(Throwable e) {}

                        @Override
                        public void onComplete() {}
                    });

        }

        @Override
        public void onLottieClickListener(final CommentItem item, LottieAnimationView lottie) {
           if(item.isClapsMe())
           {
               lottie.setAnimation("like_gray.json");
               int count = Integer.parseInt(item.clapsCount.get());
               --count;
               item.clapsCount.set(String.valueOf(count));
               item.deleteClaps(UserInfo.getInstance().userInfoItem.getNickName());
           }
           else
           {
               lottie.setAnimation("like_red.json");
               int count = Integer.parseInt(item.clapsCount.get());
               ++count;
               item.clapsCount.set(String.valueOf(count));
               lottie.playAnimation();
               item.addClaps(UserInfo.getInstance().userInfoItem.getNickName());
           }

            ApolloClient apolloClienmt = ApolloClientObject.getApolloClient();

            ToggleClapMutation mutation =  ToggleClapMutation
                    .builder()
                    .id(item.getId())
                    .build();
            ApolloCall<ToggleClapMutation.Data> apolloCall1 = apolloClienmt.mutate(mutation);
            Observable<Response<ToggleClapMutation.Data>> observable = Rx2Apollo.from(apolloCall1);

            disposable = new CompositeDisposable();

            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ToggleClapMutation.Data>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }
                        @Override
                        public void onNext(Response<ToggleClapMutation.Data> dataResponse) {
                            if(dataResponse == null) {
                                Toast.makeText(context, "예기치 못한 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                                return;
                            }
                            if(dataResponse.errors().size() > 0) {
                                Toast.makeText(context, dataResponse.errors().get(0).toString(), Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                        @Override
                        public void onError(Throwable e) {}

                        @Override
                        public void onComplete() {}
                    });
        }

        //callAPI
    }
}
