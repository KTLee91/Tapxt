package com.springsthursday.tapxt.presenter;

import android.app.ActionBar;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.airbnb.lottie.LottieAnimationView;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.rx2.Rx2Apollo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.bumptech.glide.request.transition.Transition;
import com.springsthursday.tapxt.Code.Code;
import com.springsthursday.tapxt.SeeEpisodeQuery;
import com.springsthursday.tapxt.ToggleEpisodeLikeMutation;
import com.springsthursday.tapxt.ToggleFollowMutation;
import com.springsthursday.tapxt.BindingAdapter.ContentAdapter;
import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.constract.ContentContract;
import com.springsthursday.tapxt.item.ContentItem;
import com.springsthursday.tapxt.listener.ContentListener;
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
import java.util.HashMap;
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

    public ObservableField<String> cover;
    public ObservableField<String> avatar;
    public ObservableField<String> nickName;
    public ObservableField<Integer> nextBtnVisibility;

    //region General Field
    private ArrayList<ContentItem> contentList;
    private ContentContract.View activity;
    private int contentsequence = 0;
    private int indexContentOfScene = 0;
    private int currentScene = 0;
    public CompositeDisposable disposable;
    private RecyclerView recyclerView;
    private Context context;
    private Timer timer;
    private boolean isAutoLoadContent = false;
    private boolean isLiked = false;
    private String contentID;
    private ViewFlipper flipper;
    private ToggleButton likeToggle;
    private LottieAnimationView loderLottie;
    private String episodeID = "";
    private AppBarLayout appbar;
    private String nextEpisodeID="";
    private HashMap<String, View> backupLastBackground;
    //endregion

    public ContentPresenter(Context context,ContentContract.View view ,String contentID) {
        this.context = context;
        this.contentID = contentID;
        activity = view;

        contentList = new ArrayList<>();
        loderVisibility = new ObservableInt(View.GONE);
        adapter = new ObservableField<>();
        bindingContentList = new ObservableArrayList<>();
        background = new ObservableField<>();
        storyTitle = new ObservableField<>();
        episodeTitle = new ObservableField<>();
        appbarExpanded = new ObservableField<>(false);
        cover = new ObservableField<>();
        avatar = new ObservableField<>();
        nickName = new ObservableField<>();
        nextBtnVisibility = new ObservableField<>(View.VISIBLE);
        backupLastBackground = new HashMap<>();

        adapter.set(new ContentAdapter(new ContextPreSceneClickListener()));
        adapter.get().setHasStableIds(true);
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void setViewFlipper(ViewFlipper flipper) {
        this.flipper = flipper;
    }

    public void setLikeToggle(ToggleButton like) {
        this.likeToggle = like;
    }

    public void setLoderLottie(LottieAnimationView lottie) {
        this.loderLottie = lottie;
    }

    public void setAppbar(AppBarLayout appbar) {
        this.appbar = appbar;
    }

    public void inqueryContent() {
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
                        int nextSequence = dataResponse.data().seeEpisode().sequence() + 1;

                        avatar.set(dataResponse.data().seeEpisode().creator().avatar());
                        nickName.set(dataResponse.data().seeEpisode().creator().nickname());
                        cover.set(dataResponse.data().seeEpisode().story().cover());

                        for(int j=0; j < dataResponse.data().seeEpisode().story().episodes().size(); j++)
                        {
                            if(nextSequence == dataResponse.data().seeEpisode().story().episodes().get(j).sequence())
                                nextEpisodeID = dataResponse.data().seeEpisode().story().episodes().get(j).id();
                        }

                        contentList = ContentRepostory.getInstance().LoadContentList(dataResponse);

                        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new RecyclerViewTouchListener()));

                        if (isLiked == true)
                            likeToggle.setBackground(context.getDrawable(R.drawable.ic_selectdheart));

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
    public void showExceptionErrorMessage() {
        Toast.makeText(context.getApplicationContext(), "예상치 못한 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
    }

    public void showResponseServerErrorMessage(String message) {
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    //endregion

    //region load Content Methods
    public void contextScene(final ContentItem item) {
        ++currentScene;
        adapter.set(new ContentAdapter(new ContextPreSceneClickListener()));
        bindingContentList.clear();

        if(currentScene != 1)
        {
            View currentView = flipper.getCurrentView();
            int preScene = currentScene -1;

            if(!backupLastBackground.containsKey(String.valueOf(preScene))) {
                if (currentView != null) {
                    backupLastBackground.put(String.valueOf(preScene), currentView);
                } else {
                    backupLastBackground.put(String.valueOf(preScene), getDefaultBackgroundView());
                }
            }
        }

        if(currentScene == 1) {
            bindingContentList.add(0, ContentRepostory.getInstance().getDummyContent());
            indexContentOfScene = 0;
        }
        else
        {
            bindingContentList.add(0, ContentRepostory.getInstance().getContextPreSceneType());
            bindingContentList.add(1, ContentRepostory.getInstance().getDummyContent());
            indexContentOfScene = 1;
        }
        //background.set(item.getSceneBackground());
    }

    public void contextPreScene()
    {
        currentScene--;
        adapter.set(new ContentAdapter(new ContextPreSceneClickListener()));
        bindingContentList.clear();

        ArrayList<ContentItem> itemList = new ArrayList<>();
        for(int i =0; i<contentList.size(); i++)
        {
            ContentItem item = contentList.get(i);
            if(item.getContentType() != Code.ContentType.IMPACT_BOTTOM_BACKGROUND &&
                    item.getContentType() != Code.ContentType.IMPACT_COVER_BACKGROUND &&
                    item.getSceneSequence() == currentScene)
            {
                itemList.add(item);
            }
        }

        View currentView = flipper.getCurrentView();

        if(currentView != null)
            flipper.removeView(currentView);

        flipper.addView(backupLastBackground.get(String.valueOf(currentScene)));
        flipper.showNext();

        if(currentScene == 1) {
            bindingContentList.add(0, ContentRepostory.getInstance().getDummyContent());
            bindingContentList.addAll(0, itemList);
        }
        else
        {
            bindingContentList.add(0, ContentRepostory.getInstance().getContextPreSceneType());
            bindingContentList.add(1, ContentRepostory.getInstance().getDummyContent());

            bindingContentList.addAll(1, itemList);
        }

        indexContentOfScene = itemList.size() -1;

        ContentItem item = new ContentItem();
        item = itemList.get(itemList.size() - 1);
        contentsequence = contentList.indexOf(item) + 1;
    }

    public void loadContent() {
        //indexContentOfScene : adapterList에서 현재 컨텐츠가 삽입될 위치
        //contentsequence : 전체 콘텐트 리스에서 로드 할 콘텐트의 위치

        appbar.setExpanded(false);
        ContentItem item;

        if (contentList.size() == contentsequence)
        {
            if(nextEpisodeID.equals(""))
                nextBtnVisibility.set(View.GONE);

            activity.showEpisodeDialog();
            return;
        }
        item = contentList.get(contentsequence);

        if(item.getContentType() == Code.ContentType.IMPACT_COVER_BACKGROUND)
        {
            loadBackground(false, item.getBackground(), item.getColor());
            contentsequence++;
            return;
        }
        else if(item.getContentType() == Code.ContentType.IMPACT_BOTTOM_BACKGROUND)
        {
            loadBackground(true, item.getBackground(), item.getColor());
            contentsequence++;
            return;
        }

        if (item.getContextScene() == true) {
            this.contextScene(item);
        }

        bindingContentList.add(indexContentOfScene, item);
        indexContentOfScene++;
        contentsequence++;
    }
    //endregion

    //region RecyclerView Touch Listener
    public interface ClickListener {
        void onClick();

        void onLongClick();
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
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
                    View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    View exactView = null;

                    if(childView != null)
                        exactView = findExactChild(childView, e.getX(), e.getY());

                    if(exactView != null)
                    {
                        if(exactView.getId() == R.id.btn)
                        {
                            return false;
                        }
                    }
                    clickListener.onClick();
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    if (clickListener != null) {
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

    public static View findExactChild(View childView, float x, float y){
        if(!(childView instanceof ViewGroup)) return childView;
        ViewGroup group = (ViewGroup) childView;
        final int count = group.getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View child = group.getChildAt(i);

            final float translationX = child.getTranslationX();
            final float translationY = child.getTranslationY();

            if (x >= child.getX() + translationX - 20 &&
                    x <= child.getX() + child.getWidth() + 20 + translationX &&
                    y >= child.getY() + translationY  -20 &&
                    y <= child.getY()+ child.getHeight() + 20 +translationY) {
                return child;
            }
        }
        return null;
    }

    public class RecyclerViewTouchListener implements ClickListener {
        @Override
        public void onClick() {
            loadContent();
        }

        @Override
        public void onLongClick() {
            if (isAutoLoadContent == false) {
                isAutoLoadContent = true;
                autoLoadContent();
            } else {
                isAutoLoadContent = false;
                autoLoadContentStop();
            }
        }
    }

    public void likeClickListener(View view) {
        if (isLiked == true) {
            likeToggle.setBackground(context.getDrawable(R.drawable.ic_unselectedheart));
            isLiked = false;
        } else {
            likeToggle.setBackground(context.getDrawable(R.drawable.ic_selectdheart));
            isLiked = true;
        }
        toggleEpisodeLike();
    }

    public class ContextPreSceneClickListener implements ContentListener{
        @Override
        public void preSceneClick() {
           contextPreScene();
        }
    }
    //endregion

    //region Timer On/Off Method
    public void autoLoadContent() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                loadContent();
            }
        }, 0, 1000);
    }

    public void autoLoadContentStop() {
        timer.cancel();
    }
    //endregion

    //region toggleEdpisode Like
    public void toggleEpisodeLike() {
        ApolloClient apolloClienmt = ApolloClientObject.getApolloClient();

        ToggleEpisodeLikeMutation mutation = ToggleEpisodeLikeMutation
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
                        if (dataResponse == null) {
                            Toast.makeText(context, "예기치 못한 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (dataResponse.errors().size() > 0) {
                            Toast.makeText(context, dataResponse.errors().get(0).toString(), Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
    //endregion

    public void loadBackground(boolean isBottom, String background, String color) {
        View view;

        if (isBottom == true) {
            view = LayoutInflater.from(context).inflate(R.layout.view_bottom_background, flipper, false);

            Glide.with(flipper.getContext())
                    .load(background)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            ImageView imageview = view.findViewById(R.id.background);
                            View gradientView = view.findViewById(R.id.viewGradient);

                            imageview.setBackground(resource);

                            view.setBackgroundColor(Color.parseColor(color));

                            GradientDrawable drawable = (GradientDrawable)gradientView.getBackground();
                            int[] d = {Color.parseColor(color),Color.parseColor("#00000000")};
                            drawable.setColors(d);

                            gradientView.setBackground(drawable);

                            View currentView = flipper.getCurrentView();
                            flipper.addView(view);
                            flipper.showNext();

                            if(currentView != null)
                                flipper.removeView(currentView);
                        }
                    });
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.view_all_background, flipper, false);

            Glide.with(flipper.getContext())
                    .load(background)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            ImageView imageview = view.findViewById(R.id.background);
                            imageview.setBackground(resource);

                            View currentView = flipper.getCurrentView();
                            flipper.addView(view);
                            flipper.showNext();

                            if(currentView != null)
                                flipper.removeView(currentView);
                        }
                    });
        }


    }

    public void nextBtn(View view)
    {
        activity.openNextBtn(nextEpisodeID);
    }

    public void showEpisodeList(View view)
    {
        activity.openEpisodeList();
    }

    public void showCommentList(View view)
    {
        activity.openCommentList();
    }

    public void close(View view)
    {
        activity.closeDialog();
    }

    public View getDefaultBackgroundView()
    {
        View view = LayoutInflater.from(context).inflate(R.layout.view_all_background, flipper, false);
        ImageView imageview = view.findViewById(R.id.background);
        imageview.setBackgroundColor(context.getResources().getColor(R.color.background, null));
        return view;
    }
}

