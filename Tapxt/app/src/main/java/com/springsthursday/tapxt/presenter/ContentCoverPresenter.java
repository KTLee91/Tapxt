package com.springsthursday.tapxt.presenter;

import android.content.Context;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.airbnb.lottie.LottieAnimationView;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.rx2.Rx2Apollo;
import com.springsthursday.tapxt.BindingAdapter.ContentCoverAdapter;
import com.springsthursday.tapxt.Code.Code;
import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.SeeStoryQuery;
import com.springsthursday.tapxt.ToggleFollowMutation;
import com.springsthursday.tapxt.listener.Listener;
import com.springsthursday.tapxt.constract.ContentCoverContract;
import com.springsthursday.tapxt.item.EpisodeItem;
import com.springsthursday.tapxt.item.StoryItem;
import com.springsthursday.tapxt.repository.BannerRepository;
import com.springsthursday.tapxt.util.ApolloClientObject;
import com.springsthursday.tapxt.util.Sort;

import java.util.ArrayList;
import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ContentCoverPresenter {
    public ObservableField<String> coverImage;
    public ObservableField<String> toolbarTitle;
    public ObservableField<ContentCoverAdapter> adapter;
    public ObservableField<StoryItem> items;
    public ObservableField<Integer> loderVisibility;
    public ObservableField<String> appbarTitle;

    private ContentCoverContract.View activity;
    private LottieAnimationView loderLottie;
    private CompositeDisposable disposable;
    private String storyTitle;
    private Context context;

    public ContentCoverPresenter(ContentCoverContract.View activity, String storyTitle, Context context)
    {
        this.activity = activity;
        this.context = context;

        coverImage = new ObservableField<>();
        adapter = new ObservableField<>(new ContentCoverAdapter(new ItemClickListener(), context));
        items = new ObservableField<>();
        toolbarTitle = new ObservableField<>();
        coverImage = new ObservableField<>();
        loderVisibility = new ObservableField<>(View.VISIBLE);
        appbarTitle = new ObservableField<>();

        this.storyTitle = storyTitle;

    }

    public void setLoderLottie(LottieAnimationView lottie)
    {
        this.loderLottie = lottie;
    }

    public void loadStoryItem()
    {
        loderVisibility.set(View.VISIBLE);
        loderLottie.playAnimation();

        ApolloClient apolloClienmt = ApolloClientObject.getApolloClient();

        SeeStoryQuery query = SeeStoryQuery.builder().title(storyTitle).build();
        ApolloCall<SeeStoryQuery.Data> apolloCall1 = apolloClienmt.query(query);
        Observable<Response<SeeStoryQuery.Data>> observable = Rx2Apollo.from(apolloCall1);

        disposable = new CompositeDisposable();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<SeeStoryQuery.Data>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }
                    @Override
                    public void onNext(Response<SeeStoryQuery.Data> dataResponse) {
                        if(dataResponse == null) return;
                        if(dataResponse.errors().size() > 0) return;

                        StoryItem item = new StoryItem();

                        item.setNickName(dataResponse.data().seeStory().creator().nickname());
                        item.setAvator(dataResponse.data().seeStory().creator().avatar());

                        item.setTitle(dataResponse.data().seeStory().title());
                        appbarTitle.set(item.getTitle());
                        item.setCover(dataResponse.data().seeStory().cover());
                        item.setDescription(dataResponse.data().seeStory().description());
                        item.setFollowed(dataResponse.data().seeStory().creator().isFollowed());

                        for(int i=0; i<dataResponse.data().seeStory().episodes().size(); i++)
                        {
                            SeeStoryQuery.Episode episode = dataResponse.data().seeStory().episodes().get(i);

                            EpisodeItem episodeItem = new EpisodeItem();
                            episodeItem.setSequence(episode.sequence());
                            episodeItem.setEpidoseId(episode.id());
                            episodeItem.setTitle(episode.title());
                            episodeItem.setLiked(episode.isLiked());
                            episodeItem.setCreatedAt(episode.createdAt());
                            episodeItem.setCommentCount(episode.comments().size());
                            episodeItem.setLikeCount(episode.likesCount());
                            episodeItem.setInquiryCount(episode.viewCount());

                            item.addEpisodeItem(episodeItem);
                       }

                        coverImage.set(item.getCover());
                        items.set(item);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("dd","dd");
                    }

                    @Override
                    public void onComplete() {}
                });
    }

    public class ItemClickListener implements Listener.OnItemClickListener{
        @Override
        public void onItemClick(EpisodeItem item)
        {
           activity.openContentActivity(item.getEpidoseId());
        }

        @Override
        public void onTextClick(int orderBy)
        {
            ArrayList<EpisodeItem> itemList = items.get().getEpisodeList();

            if(orderBy == Code.Orderby.ORDER_BY_FIRST)
                Collections.sort(itemList, new Sort.AscSorting<EpisodeItem>());
            else
                Collections.sort(itemList, new Sort.DescdingSort<EpisodeItem>());

            adapter.get().notifyDataSetChanged();
        }

        @Override
        public void onAlarmClick(ToggleButton lottie) {

        }

        @Override
        public void onFollowClick(ToggleButton togglebtn, StoryItem item) {
            if(item.isFollowed() == true)
            {
                togglebtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_unselectedheart, null));
                item.setFollowed(false);
            }
            else
            {
                togglebtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_selectdheart, null));
                item.setFollowed(true);
            }
            toggleFollow(item.getNickName());
        }

        @Override
        public void onCommentClick(EpisodeItem item) {
            activity.openCommentActivity(item.getEpidoseId());
        }
    }

    public void toggleFollow(String nickName)
    {
        ApolloClient apolloClienmt = ApolloClientObject.getApolloClient();

        ToggleFollowMutation mutation =  ToggleFollowMutation
                .builder()
                .nickname(nickName)
                .build();
        ApolloCall<ToggleFollowMutation.Data> apolloCall1 = apolloClienmt.mutate(mutation);
        Observable<Response<ToggleFollowMutation.Data>> observable = Rx2Apollo.from(apolloCall1);

        disposable = new CompositeDisposable();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ToggleFollowMutation.Data>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }
                    @Override
                    public void onNext(Response<ToggleFollowMutation.Data> dataResponse) {
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
