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
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
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
import com.springsthursday.tapxt.util.AnimationUtil;
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
    public ObservableField<Integer> autoLoadViewVisibility;

    //region General Field
    private ArrayList<ContentItem> contentList;
    private ContentContract.View activity;
    private int contentsequence = 0;
    private int indexContentOfScene = 0;
    private int currentScene = 0;
    public CompositeDisposable disposable;
    private RecyclerView recyclerView;
    private Context context;
    private boolean isLiked = false;
    private String contentID;
    private ViewFlipper flipper;
    private ToggleButton likeToggle;
    private LottieAnimationView loderLottie;
    private String episodeID = "";
    private AppBarLayout appbar;
    private String nextEpisodeID = "";
    private HashMap<String, String> backupLastBackground;
    private HashMap<String, String> sceneBackgroudSound;
    private AnimationSet animationSet;
    private View currentView;
    private int impactIndex = 0;
    public MediaPlayer backgroundSoundPlayer;
    private String currentBackgroundSound;
    //endregion

    public ContentPresenter(Context context, ContentContract.View view, String contentID) {
        this.context = context;
        this.contentID = contentID;
        activity = view;
        currentBackgroundSound = "";

        contentList = new ArrayList<>();
        loderVisibility = new ObservableInt(View.GONE);
        adapter = new ObservableField<>();
        background = new ObservableField<>();
        storyTitle = new ObservableField<>();
        episodeTitle = new ObservableField<>();
        appbarExpanded = new ObservableField<>(false);
        cover = new ObservableField<>();
        avatar = new ObservableField<>();
        nickName = new ObservableField<>();
        nextBtnVisibility = new ObservableField<>(View.VISIBLE);
        autoLoadViewVisibility = new ObservableField<>(View.GONE);

        backupLastBackground = new HashMap<>();
        sceneBackgroudSound = new HashMap<>();
        animationSet = new AnimationSet(false);

        adapter.set(new ContentAdapter(new ContextPreSceneClickListener()));
        adapter.get().setHasStableIds(true);
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void setViewFlipper(ViewFlipper flipper) {
        this.flipper = flipper;

        Animation fadeIn = AnimationUtil.getFadeIn();
        Animation fadeOut = AnimationUtil.getFadeOut();

        animationSet.addAnimation(fadeIn);
        animationSet.addAnimation(fadeOut);

        this.flipper.setInAnimation(fadeIn);
        this.flipper.setOutAnimation(fadeOut);
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

    public void setAutoLoadViewVisibility(int visibility)
    {
        this.autoLoadViewVisibility.set(visibility);
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

                        for (int j = 0; j < dataResponse.data().seeEpisode().story().episodes().size(); j++) {
                            if (nextSequence == dataResponse.data().seeEpisode().story().episodes().get(j).sequence())
                                nextEpisodeID = dataResponse.data().seeEpisode().story().episodes().get(j).id();
                        }

                        contentList = ContentRepostory.getInstance().LoadContentList(dataResponse);

                        //실제 서버 연동 코드
                       /* for(int k =0; k < dataResponse.data().seeEpisode().scenes().size(); k++)
                        {
                            SeeEpisodeQuery.Scene scene = dataResponse.data().seeEpisode().scenes().get(k);
                            sceneBackgroudSound.put(String.valueOf(k+1), scene.sceneProperty().sound());
                        }*/

                        sceneBackgroudSound.put(String.valueOf(1), "https://s3.ap-northeast-2.amazonaws.com/tapxt.src/audio1.mp3");
                        sceneBackgroudSound.put(String.valueOf(2), "https://s3.ap-northeast-2.amazonaws.com/tapxt.src/audio2.mp3");
                        sceneBackgroudSound.put(String.valueOf(3), "https://s3.ap-northeast-2.amazonaws.com/tapxt.src/audio3.mp3");

                        activity.setTouchListener();

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
        adapter.get().removeItems();

        ArrayList<ContentItem> list = new ArrayList<>();

        if (currentScene != 1) {
            int preScene = currentScene - 1;

            if (!backupLastBackground.containsKey(String.valueOf(preScene))) {
                backupLastBackground.put(String.valueOf(preScene), String.valueOf(impactIndex - 1));

            }
        }

        currentBackgroundSound = sceneBackgroudSound.get(String.valueOf(currentScene));
        startBackgroundSound();

        for (int i = 0; i < contentList.size(); i++) {
            ContentItem current = contentList.get(i);

            if ((current.getContentType() == Code.ContentType.IMPACT_BOTTOM_BACKGROUND || current.getContentType() == Code.ContentType.IMPACT_COVER_BACKGROUND)
                    && current.getSceneSequence() == currentScene) {
                list.add(current);
            }
        }

        if (list.size() == 0) {
            if (flipper.getChildAt(impactIndex) == null) {
                flipper.addView(getDefaultBackgroundView());
                flipper.showNext();
                impactIndex++;
            } else {
                flipper.setDisplayedChild(impactIndex);
                impactIndex++;
            }
        }

        ((DefaultItemAnimator) recyclerView.getItemAnimator()).setAddDuration(500);

        if (currentScene == 1) {
            adapter.get().addItem(ContentRepostory.getInstance().getDummyContent(), 0);
            ;
            indexContentOfScene = 0;
        } else {
            ArrayList<ContentItem> itemList = new ArrayList<>();

            itemList.add(ContentRepostory.getInstance().getContextPreSceneType());
            itemList.add(ContentRepostory.getInstance().getDummyContent());

            adapter.get().addAllItems(itemList, 0);

            indexContentOfScene = 1;
        }
        ((DefaultItemAnimator) recyclerView.getItemAnimator()).setAddDuration(0);
    }

    public void contextPreScene() {
        currentScene--;

        currentBackgroundSound = sceneBackgroudSound.get(String.valueOf(currentScene));
        startBackgroundSound();

        ArrayList<ContentItem> itemList = new ArrayList<>();
        for (int i = 0; i < contentList.size(); i++) {
            ContentItem item = contentList.get(i);
            if (item.getContentType() != Code.ContentType.IMPACT_BOTTOM_BACKGROUND &&
                    item.getContentType() != Code.ContentType.IMPACT_COVER_BACKGROUND &&
                    item.getSceneSequence() == currentScene) {
                itemList.add(item);
            }
        }

        currentView = flipper.getCurrentView();

        impactIndex = Integer.parseInt(backupLastBackground.get(String.valueOf(currentScene)));
        flipper.setDisplayedChild(impactIndex);
        impactIndex++;

        backupLastBackground.remove(String.valueOf(currentScene));

        adapter.get().removeItems();

        ((DefaultItemAnimator) recyclerView.getItemAnimator()).setAddDuration(500);

        if (currentScene == 1) {
            itemList.add(ContentRepostory.getInstance().getDummyContent());
            adapter.get().addAllItems(itemList, 0);
        } else {
            itemList.add(0, ContentRepostory.getInstance().getContextPreSceneType());
            itemList.add(ContentRepostory.getInstance().getDummyContent());

            adapter.get().addAllItems(itemList, 0);
        }

        this.scrollToEndPosition();
        ((DefaultItemAnimator) recyclerView.getItemAnimator()).setAddDuration(0);

        indexContentOfScene = itemList.size() - 2;

        ContentItem item;
        item = itemList.get(itemList.size() - 2);
        contentsequence = contentList.indexOf(item) + 1;
    }

    public void loadContent() {
        //indexContentOfScene : adapterList에서 현재 컨텐츠가 삽입될 위치
        //contentsequence : 전체 콘텐트 리스에서 로드 할 콘텐트의 위치

        appbar.setExpanded(false);
        ContentItem item;

        if (contentList.size() == contentsequence) {
            if (nextEpisodeID.equals(""))
                nextBtnVisibility.set(View.GONE);

            activity.autoLoadContentStop();
            activity.showEpisodeDialog();
            return;
        }
        item = contentList.get(contentsequence);

        View view = flipper.getChildAt(impactIndex);

        if (item.getContentType() == Code.ContentType.IMPACT_COVER_BACKGROUND) {
            if (view != null) {
                flipper.setDisplayedChild(impactIndex);
            } else {
                loadBackground(false, item.getBackground(), item.getColor());
            }

            impactIndex++;
            contentsequence++;
            return;

        } else if (item.getContentType() == Code.ContentType.IMPACT_BOTTOM_BACKGROUND) {
            if (view != null) {
                flipper.setDisplayedChild(impactIndex);
            } else {
                loadBackground(true, item.getBackground(), item.getColor());
            }

            impactIndex++;
            contentsequence++;
            return;
        }

        if (item.getContextScene() == true) {
            this.contextScene(item);
        }

        adapter.get().addItem(item, indexContentOfScene);
        this.scrollToEndPosition();

        indexContentOfScene++;
        contentsequence++;
    }
    //endregion

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

    public class ContextPreSceneClickListener implements ContentListener {
        @Override
        public void preSceneClick() {
            contextPreScene();
        }
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

                            GradientDrawable drawable = (GradientDrawable) gradientView.getBackground();
                            int[] d = {Color.parseColor(color), Color.parseColor("#00000000")};
                            drawable.setColors(d);

                            gradientView.setBackground(drawable);

                            flipper.addView(view);
                            flipper.showNext();
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

                            flipper.addView(view);
                            flipper.showNext();
                        }
                    });
        }


    }

    public void nextBtn(View view) {
        activity.openNextBtn(nextEpisodeID);
    }

    public void showEpisodeList(View view) {
        activity.openEpisodeList();
    }

    public void showCommentList(View view) {
        activity.openCommentList();
    }

    public void close(View view) {
        activity.closeDialog();
    }

    public View getDefaultBackgroundView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_all_background, flipper, false);
        ImageView imageview = view.findViewById(R.id.background);
        imageview.setBackgroundColor(context.getResources().getColor(R.color.background, null));

        return view;
    }

    public void scrollToEndPosition() {
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                recyclerView.scrollToPosition(adapter.get().getItemCount() - 1);
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public void startBackgroundSound()
    {
        if(currentBackgroundSound == null || currentBackgroundSound.isEmpty()) return;

        try {
            if(backgroundSoundPlayer == null)
                backgroundSoundPlayer = new MediaPlayer();
            else
                backgroundSoundPlayer.reset();

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            backgroundSoundPlayer.setAudioAttributes(audioAttributes);
            backgroundSoundPlayer.setDataSource(currentBackgroundSound);
            backgroundSoundPlayer.prepareAsync();
            backgroundSoundPlayer.setLooping(true);

            backgroundSoundPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    backgroundSoundPlayer.start();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            backgroundSoundPlayer.release();
        }
    }

    public void clickTest(View view)
    {
       activity.setTimer(false, false, 0);
    }
}

