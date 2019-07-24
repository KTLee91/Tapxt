package com.springsthursday.tapxt.presenter;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.UiThread;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.airbnb.lottie.LottieAnimationView;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.rx2.Rx2Apollo;
import com.bumptech.glide.Glide;
import com.springsthursday.tapxt.SeeEpisodeQuery;
import com.springsthursday.tapxt.ToggleEpisodeLikeMutation;
import com.springsthursday.tapxt.ToggleFollowMutation;
import com.springsthursday.tapxt.BindingAdapter.ContentAdapter;
import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.item.ContentItem;
import com.springsthursday.tapxt.repository.CommentRepository;
import com.springsthursday.tapxt.repository.ContentRepostory;
import com.springsthursday.tapxt.util.ApolloClientObject;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class ContentPresenter {

    public ObservableArrayList<ContentItem> bindingContentList;
    public ObservableField<ContentAdapter> adapter;
    public ObservableInt loderVisibility;
    public ObservableField<String> background;
    public ObservableField<String> storyTitle;
    public ObservableField<String> episodeTitle;
    public ObservableField<Boolean> appbarExpanded;

    private ArrayList<ContentItem> contentList;
    private int contentsequence = 0;
    private int indexContentOfScene = 0;
    private CompositeDisposable disposable;
    private RecyclerView recyclerView;
    private Context context;
    private Timer timer;
    private boolean isAutoLoadContent = false;
    private boolean isLiked = false;
    private String contentID;
    private AppBarLayout appbar;
    private ImageView imageView;
    private LottieAnimationView lottie;
    private LottieAnimationView loderLottie;
    private String episodeID ="";

    public ContentPresenter(Context context, String contentID)
    {
        this.context = context;
        this.contentID = contentID;

        contentList = new ArrayList<>();
        loderVisibility = new ObservableInt(View.GONE);
        adapter = new ObservableField<>();
        bindingContentList = new ObservableArrayList<>();
        background = new ObservableField<>();
        storyTitle = new ObservableField<>();
        episodeTitle = new ObservableField<>();
        appbarExpanded = new ObservableField<>(false);

        adapter.set(new ContentAdapter());
        adapter.get().setHasStableIds(true);
    }

    public void setRecyclerView(RecyclerView recyclerView)
    {
        this.recyclerView = recyclerView;
    }

    public void setAppBar(AppBarLayout appBar) {
        this.appbar = appBar;
    }

    public void setImageView(ImageView imageView)
    {
        this.imageView = imageView;
    }

    public void setLottie(LottieAnimationView lottie) {this.lottie = lottie;}

    public void setLoderLottie(LottieAnimationView lottie) {this.loderLottie = lottie;}

    public void inqueryContent()
    {
        loderVisibility.set(View.VISIBLE);
        loderLottie.playAnimation();

        ApolloClient apolloClient = ApolloClientObject.getApolloClient();

        SeeEpisodeQuery query = SeeEpisodeQuery.builder().id(contentID).build();
        ApolloCall<SeeEpisodeQuery.Data> apolloCall1 = apolloClient.query(query);
        Observable<Response<SeeEpisodeQuery.Data>> observable = Rx2Apollo.from(apolloCall1);

        disposable = new CompositeDisposable();

        observable.subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<SeeEpisodeQuery.Data>>() {
            @Override
            public void onSubscribe(Disposable d) {
                  disposable.add(d);
            }
            @Override
            public void onNext(Response<SeeEpisodeQuery.Data> dataResponse) {
                if (dataResponse == null) {
                    showExceptionErrorMessage();
                    loderVisibility.set(View.GONE);
                    return;
                }
                if (dataResponse.errors().size() > 0) {
                    showResponseServerErrorMessage(dataResponse.errors().get(0).toString());
                    loderVisibility.set(View.GONE);
                    return;
                }

                storyTitle.set(dataResponse.data().seeEpisode().story().title());
                episodeTitle.set(dataResponse.data().seeEpisode().title());
                isLiked = dataResponse.data().seeEpisode().isLiked();
                episodeID = dataResponse.data().seeEpisode().id();

                CommentRepository.getInstance().initializeComment(dataResponse);
                contentList = ContentRepostory.getInstance().LoadContentList(dataResponse);

                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new RecyclerViewTouchListener()));

                if(isLiked == true)
                    lottie.setAnimation("like_red.json");

                loadContent();

                loderVisibility.set(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("error", e.toString());
            }

            @Override
            public void onComplete() {
            }
        });
    }

    //region show Error Message
    public void showExceptionErrorMessage()
    {
        Toast.makeText(context.getApplicationContext(), "예상치 못한 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
    }

    public void showResponseServerErrorMessage(String message)
    {
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    //endregion

    //region load Content Methods
    public void contextScene(final ContentItem item){

        adapter.set(new ContentAdapter());

        bindingContentList.clear();
        indexContentOfScene = 0;
        bindingContentList.add(0,ContentRepostory.getInstance().getDummyContent());
        background.set(item.getSceneBackground());
       // Glide.with(context).load(item.getSceneBackground()).crossFade(5000).animate(android.R.anim.slide_in_left).skipMemoryCache(true).into(imageView);
    }

    public void loadContent()  {
        appbarExpanded.set(false);
        ContentItem item;

        if(contentList.size() == contentsequence) return;
        item = contentList.get(contentsequence);

        if(item.getContextScene() == true) {
        this.contextScene(item);
    }

        bindingContentList.add(indexContentOfScene, item);
        indexContentOfScene++;
        contentsequence ++;
    }
    //endregion

    //region RecyclerView Touch Listener
    public interface ClickListener
    {
        void onClick();

        void onLongClick();
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener
    {
        private GestureDetector gestureDetector;
        private ContentPresenter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ContentPresenter.ClickListener clickListener) {
            this.clickListener = clickListener;

            gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
                @Override
                public void onShowPress(MotionEvent e) {

                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    return false;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    return false;
                }
                @Override
                public boolean onDown(MotionEvent e) {
                     return false;
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    clickListener.onClick();

                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    if(clickListener != null)
                    {
                        clickListener.onLongClick();
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            return this.gestureDetector.onTouchEvent(e);
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    public class RecyclerViewTouchListener implements ClickListener
    {
        @Override
        public void onClick() {
            loadContent();
        }

        @Override
        public void onLongClick() {
            if(isAutoLoadContent == false) {
                isAutoLoadContent = true;
                autoLoadContent();
            }
            else {
                isAutoLoadContent = false;
                autoLoadContentStop();
            }
        }
    }

    public void lottieClickListener(View view)
    {
        if(isLiked == true)
        {
            isLiked = false;
            lottie.setAnimation("like_gray.json");
        }
        else
        {
            isLiked = true;
            lottie.setAnimation("like_red.json");
        }
        toggleEpisodeLike();
    }
    //endregion

    //region Timer On/Off Method
    public void autoLoadContent()
    {
        timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                loadContent();
            }
        }, 0 , 1000);
    }

    public void autoLoadContentStop()
    {
        timer.cancel();
    }
    //endregion

    public void toggleEpisodeLike()
    {
        ApolloClient apolloClienmt = ApolloClientObject.getApolloClient();

        ToggleEpisodeLikeMutation mutation =  ToggleEpisodeLikeMutation
                .builder()
                .id(episodeID)
                .build();

        ApolloCall<ToggleEpisodeLikeMutation.Data> apolloCall1 = apolloClienmt.mutate(mutation);
        Observable<Response<ToggleEpisodeLikeMutation.Data>> observable = Rx2Apollo.from(apolloCall1);

        disposable = new CompositeDisposable();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ToggleEpisodeLikeMutation.Data>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }
                    @Override
                    public void onNext(Response<ToggleEpisodeLikeMutation.Data> dataResponse) {
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
