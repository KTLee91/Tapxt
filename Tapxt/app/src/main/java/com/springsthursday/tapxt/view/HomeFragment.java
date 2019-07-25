package com.springsthursday.tapxt.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.springsthursday.tapxt.BindingAdapter.MainBannerAdapter;
import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.constract.HomeContract;
import com.springsthursday.tapxt.databinding.FragmentHomeBinding;
import com.springsthursday.tapxt.item.StoryItem;
import com.springsthursday.tapxt.presenter.HomePresenter;
import com.springsthursday.tapxt.repository.UserInfo;

import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment implements HomeContract.View {
    private HomePresenter viewModel;
    private LottieAnimationView lottie;
    private FragmentHomeBinding binding;
    private int currentPage = 0;
    private Timer timer;
    private Toolbar toolbar;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        Window window = getActivity().getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setNavigationBarColor(getActivity().getColor(R.color.background));

        viewModel = new HomePresenter(this, getActivity().getApplicationContext());

        FragmentHomeBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        return binding.getRoot();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        viewModel = new HomePresenter(this, getActivity().getApplicationContext());
        binding = DataBindingUtil.getBinding(getView());
        binding.setViewModel(viewModel);

        viewModel.setViewPager(binding.mainBanner);
        viewModel.setLoader(binding.animationView);

        binding.toolbar.getBackground().setAlpha(0);
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.setHasFixedSize(true);

        binding.toolbarlayout.setTitleEnabled(false);

        binding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0)
                {
                    binding.title.setVisibility(View.VISIBLE);
                }
                else
                {
                    binding.title.setVisibility(View.GONE);
                }
            }
        });


        viewModel.loadProfile();
        viewModel.loadMainBanner();
        viewModel.loadFeed();
    }

    @Override
    public void startAutoScroll() {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == binding.mainBanner.getAdapter().getCount()) {
                    currentPage = 0;
                }
                binding.mainBanner.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 500, 5000);
    }

    @Override
    public void openContentCover(String id) {
        Intent intent = new Intent(getActivity(), ContentCoverActivity.class);
        intent.putExtra("Story", id);
        getActivity().startActivity(intent);
    }

    @Override
    public void addIndicator() {
        binding.indicator.setViewPager(binding.mainBanner);
        binding.mainBanner.getAdapter().registerDataSetObserver(binding.indicator.getDataSetObserver());
    }
}
