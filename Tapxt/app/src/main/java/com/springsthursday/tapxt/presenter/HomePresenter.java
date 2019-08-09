package com.springsthursday.tapxt.presenter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.ObservableField;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.apollographql.apollo.rx2.Rx2Apollo;
import com.springsthursday.tapxt.BindingAdapter.MainAdapter;
import com.springsthursday.tapxt.BindingAdapter.MainBannerAdapter;
import com.springsthursday.tapxt.GetUserProfileQuery;
import com.springsthursday.tapxt.InqueryBannersQuery;
import com.springsthursday.tapxt.SeeAllStoryListQuery;
import com.springsthursday.tapxt.SeeFeedQuery;
import com.springsthursday.tapxt.constract.HomeContract;
import com.springsthursday.tapxt.item.BannerItem;
import com.springsthursday.tapxt.item.FollowItem;
import com.springsthursday.tapxt.item.StoryItem;
import com.springsthursday.tapxt.listener.StoryClickListener;
import com.springsthursday.tapxt.repository.BannerRepository;
import com.springsthursday.tapxt.repository.MainRepository;
import com.springsthursday.tapxt.repository.UserInfo;
import com.springsthursday.tapxt.util.ApolloClientObject;

import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class HomePresenter {
    public ObservableField<Integer> loaderVisibility;
    public ObservableField<Integer> btnVisibility;
    public ObservableField<MainBannerAdapter> adapter;
    public ObservableField<ArrayList<BannerItem>> items;
    public ObservableField<ArrayList<ArrayList<StoryItem>>> allStoryList;
    public ArrayList<ArrayList<StoryItem>> tmpStoryList;
    public ObservableField<MainAdapter> mainAdapter;

    private HomeContract.View activity;
    public CompositeDisposable disposable;
    private LottieAnimationView lottie;
    private Context context;
    private GestureDetector gestureDetector = null;
    private ViewPager viewPager = null;

    public HomePresenter(HomeContract.View view, Context context)
    {
        this.context = context;

        loaderVisibility = new ObservableField<>(View.GONE);
        btnVisibility = new ObservableField<>(View.GONE);
        adapter = new ObservableField<>(new MainBannerAdapter(context));
        items = new ObservableField<>();
        mainAdapter = new ObservableField<>(new MainAdapter(context, new StoryItemClickListener()));
        allStoryList = new ObservableField<>();
        mainAdapter.get().setHasStableIds(true);

        gestureDetector = new GestureDetector( context, new SingleTapGestureListener() );
        tmpStoryList = new ArrayList<ArrayList<StoryItem>>();
        disposable = new CompositeDisposable();

        disposable = new CompositeDisposable();
        this.activity = view;
    }

    public void setLoader(LottieAnimationView lottie)
    {
        this.lottie = lottie;
    }

    public void setViewPager(ViewPager viewPager) {this.viewPager = viewPager;}

    //region Load Main Banner
    public void loadMainBanner()
    {
        loaderVisibility.set(View.VISIBLE);
        lottie.playAnimation();

        ApolloClient apolloClienmt = ApolloClientObject.getApolloClient();

        InqueryBannersQuery query = InqueryBannersQuery.builder().build();
        ApolloCall<InqueryBannersQuery.Data> apolloCall1 = apolloClienmt.query(query);
        Observable<Response<InqueryBannersQuery.Data>> observable = Rx2Apollo.from(apolloCall1);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<InqueryBannersQuery.Data>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }
                    @Override
                    public void onNext(Response<InqueryBannersQuery.Data> dataResponse) {

                        loaderVisibility.set(View.GONE);

                        if(dataResponse == null) {
                            Toast.makeText(context,"예기치 못한 오류가 발생했습니다. 잠시 후 다시 시도해주세요",Toast.LENGTH_LONG );
                            return;
                        }
                        if(dataResponse.errors().size() > 0){
                            Toast.makeText(context,dataResponse.errors().get(0).toString(),Toast.LENGTH_LONG );
                            return;
                        }

                        BannerRepository.getInstance().initializeBannerItems(dataResponse);
                        items.set(BannerRepository.getInstance().getBannerItems());

                        loaderVisibility.set(View.GONE);

                        viewPager.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                gestureDetector.onTouchEvent(event);
                                return true;
                            }
                        });

                        activity.addIndicator();
                        activity.startAutoScroll();
                    }

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onComplete() {}
                });
    }
    //endregion

    //region Load Feed Data
    public void loadFeed()
    {
        loaderVisibility.set(View.VISIBLE);
        ApolloClient apolloClienmt = ApolloClientObject.getApolloClient();

        SeeFeedQuery query = SeeFeedQuery.builder().build();
        ApolloCall<SeeFeedQuery.Data> apolloCall1 = apolloClienmt.query(query);
        Observable<Response<SeeFeedQuery.Data>> observable = Rx2Apollo.from(apolloCall1);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<SeeFeedQuery.Data>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }
                    @Override
                    public void onNext(Response<SeeFeedQuery.Data> dataResponse) {
                        if(dataResponse == null) {
                            Toast.makeText(context,"예기치 못한 오류가 발생했습니다. 잠시 후 다시 시도해주세요",Toast.LENGTH_LONG );
                            return;
                        }

                        if(dataResponse.errors().size() > 0){
                            Toast.makeText(context,dataResponse.errors().get(0).toString(),Toast.LENGTH_LONG );
                            return;
                        }
                        Log.d("Feed",String.valueOf(disposable.size()));
                        allStoryList.set(MainRepository.getInstance().loadFeedItemList(dataResponse));
                        allStoryList.notifyChange();
                    }

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onComplete() {}
                });
    }
    //endregion

    public void setAllStoryList(Response<SeeAllStoryListQuery.Data> response)
    {
        MainRepository.getInstance().loadAllStroyList(response);
    }

    public void SetFeed(Response<SeeFeedQuery.Data> response)
    {
        MainRepository.getInstance().loadFeedItemList(response);
    }

    public void handleError()
    {
        Toast.makeText(context, "요청 중 에러가 발생했습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_LONG).show();
    }

    @SuppressLint("CheckResult")
    public void loadAllStoryList() {
        loaderVisibility.set(View.VISIBLE);

        ApolloClient apolloClienmt = ApolloClientObject.getApolloClient();

        SeeAllStoryListQuery query = SeeAllStoryListQuery.builder().build();
        ApolloCall<SeeAllStoryListQuery.Data> apolloCall1 = apolloClienmt.query(query);
        Observable<Response<SeeAllStoryListQuery.Data>> observable = Rx2Apollo.from(apolloCall1);

        ApolloClient apolloClienmt2 = ApolloClientObject.getApolloClient();

        SeeFeedQuery query2 = SeeFeedQuery.builder().build();
        ApolloCall<SeeFeedQuery.Data> apolloCall2 = apolloClienmt2.query(query2);
        Observable<Response<SeeFeedQuery.Data>> observable2 = Rx2Apollo.from(apolloCall2);

        List<Observable<?>> observableList = new ArrayList<>();

        observableList.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(Observable :: fromArray)
                .doOnError(response->handleError())
                .doOnNext(response->setAllStoryList(response)));

        observableList.add(observable2
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(Observable :: fromArray)
                .doOnError(response->handleError())
                .doOnNext(user->SetFeed(user)));

        Observable.zip(observableList, new Function<Object, Observable<?>>() {
            @Override
            public Observable<?> apply(Object o) throws Exception {
                return Observable.empty();
            }
        }).subscribe(observable1 -> {
            allStoryList.set(MainRepository.getInstance().getItems());
            allStoryList.notifyChange();
        });
    }


    //region Listener
    private final class SingleTapGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int position = viewPager.getCurrentItem();
            try {
                if( velocityX < 0 )
                {
                    if(position == adapter.get().getCount())
                        return true;
                    else
                        viewPager.setCurrentItem(++position, true);
                }
                else
                {
                    if(position == 0)
                        return true;
                    else
                        viewPager.setCurrentItem(--position, true);
                }
            }catch ( Exception e ) {}
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            try
            {
                int position = viewPager.getCurrentItem();
                activity.openContentCover(adapter.get().getCurrentItem(position).getStoryID());
            }
            catch (Exception e )
            {}
            return true;
        }
    }

    class StoryItemClickListener implements StoryClickListener
    {
        @Override
        public void storyClick(StoryItem item) {
            activity.openContentCover(item.getStoryid());
        }
    }
    //endregion
}
