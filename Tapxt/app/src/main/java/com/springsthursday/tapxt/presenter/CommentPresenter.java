package com.springsthursday.tapxt.presenter;

import android.content.Context;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.rx2.Rx2Apollo;
import com.springsthursday.tapxt.BindingAdapter.CommentAdapter;
import com.springsthursday.tapxt.CreateCommentMutation;
import com.springsthursday.tapxt.DeleteCommentMutation;
import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.SeeCommentsQuery;
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
import io.reactivex.internal.operators.observable.ObservableRange;
import io.reactivex.schedulers.Schedulers;

public class CommentPresenter {

    public ObservableField<CommentAdapter> adapter;
    public ObservableField<ArrayList<CommentItem>> items;
    public ObservableField<String> appbarTitle;
    public ObservableField<String> editComment;
    public ObservableField<Integer> loaderVisibility;
    public ObservableField<Integer> dividerVisibility;
    public ObservableField<Integer> inputCommentVisibility;
    public ObservableField<Integer> addBtnVisibility;

    private CompositeDisposable disposable;
    private String episodeID;
    private Context context;
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
        dividerVisibility = new ObservableField<>(View.GONE);
        inputCommentVisibility = new ObservableField<>(View.GONE);
        addBtnVisibility = new ObservableField<>(View.GONE);

        adapter.set(new CommentAdapter(new ClickListener(), context));
    }

    public void loadCommentList()
    {
        loaderVisibility.set(View.VISIBLE);
        lottie.playAnimation();

        ApolloClient apolloClienmt = ApolloClientObject.getApolloClient();

        SeeCommentsQuery query =  SeeCommentsQuery
                .builder()
                .id(this.episodeID)
                .build();
        ApolloCall<SeeCommentsQuery.Data> apolloCall1 = apolloClienmt.query(query);
        Observable<Response<SeeCommentsQuery.Data>> observable = Rx2Apollo.from(apolloCall1);

        disposable = new CompositeDisposable();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<SeeCommentsQuery.Data>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }
                    @Override
                    public void onNext(Response<SeeCommentsQuery.Data> dataResponse) {
                        if(dataResponse == null) {
                            Toast.makeText(context, "예기치 못한 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(dataResponse.errors().size() > 0) {
                            Toast.makeText(context, dataResponse.errors().get(0).toString(), Toast.LENGTH_LONG).show();
                            return;
                        }
                        showView();
                        loaderVisibility.set(View.GONE);

                        items.set(CommentRepository.getInstance().getCommentList(dataResponse));
                        appbarTitle.set("댓글 (" + items.get().size() + ")");
                    }
                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onComplete() {}
                });
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
        activity.hideKeybord();
        if(editComment.get().isEmpty()) return;
        activity.showProgressDialog("댓글 생성 중...");

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

                        activity.hideProgressDialog();

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
                        appbarTitle.set("댓글 (" + adapter.get().getItemCount() + ")");
                    }
                    @Override
                    public void onError(Throwable e) {
                        activity.hideProgressDialog();
                    }

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
            activity.showProgressDialog("댓글 삭제 중...");

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

                            activity.hideProgressDialog();

                            if(dataResponse == null) {
                                Toast.makeText(context, "예기치 못한 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                                return;
                            }
                            if(dataResponse.errors().size() > 0) {
                                Toast.makeText(context, dataResponse.errors().get(0).toString(), Toast.LENGTH_LONG).show();
                                return;
                            }
                            if(dataResponse.data().deleteComment() == true) {
                                adapter.get().removeItem(item);
                                appbarTitle.set("댓글 (" + adapter.get().getItemCount()+ ")");
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

        @Override
        public void onClapsClickListener(final CommentItem item, TextView claps) {
           if(item.isClapsMe())
           {
               claps.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.left_like_white, null),null,null,null);
               int count = Integer.parseInt(item.clapsCount.get());
               --count;
               item.clapsCount.set(String.valueOf(count));
               item.deleteClaps(UserInfo.getInstance().userInfoItem.getNickName());
           }
           else
           {
               claps.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.left_like_red, null),null,null,null);
               int count = Integer.parseInt(item.clapsCount.get());
               ++count;

               item.clapsCount.set(String.valueOf(count));
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

    }
    public void showView()
    {
        addBtnVisibility.set(View.VISIBLE);
        inputCommentVisibility.set(View.VISIBLE);
        dividerVisibility.set(View.VISIBLE);
    }
    //callAPI
}
