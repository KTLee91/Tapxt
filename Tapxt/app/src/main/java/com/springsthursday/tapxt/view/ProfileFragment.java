package com.springsthursday.tapxt.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.springsthursday.tapxt.BindingAdapter.StoryListPagerAdapter;
import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.constract.ProfileContract;
import com.springsthursday.tapxt.databinding.FragmentProfileBinding;
import com.springsthursday.tapxt.presenter.ProfilePresenter;
import com.springsthursday.tapxt.util.NetWorkBrodcastReceiver;

public class ProfileFragment extends Fragment implements ProfileContract.View {
    private ProfilePresenter viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        setHasOptionsMenu(true);
        return DataBindingUtil.inflate(inflater,R.layout.fragment_profile, container, false).getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        if (!NetWorkBrodcastReceiver.getInstance(getActivity().getApplicationContext()).isOnline())
            Toast.makeText(getActivity().getApplicationContext(), "네트워크 상태가 불안정합니다",Toast.LENGTH_LONG).show();

        viewModel = new ProfilePresenter(this);
        FragmentProfileBinding binding = DataBindingUtil.getBinding(getView());
        binding.setViewModel(viewModel);

        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("읽은 작품"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("좋아요 한 작품"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        StoryListPagerAdapter pagerAdapter = new StoryListPagerAdapter(getActivity().getSupportFragmentManager(), binding.tabLayout.getTabCount());
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));

        // Set TabSelectedListener
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

   @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.menu_appbar_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void setUserInfo(String nickName, String imageUrl)
    {
        viewModel.setUserInfo(nickName, imageUrl);
    }

    public Context getContext()
    {
        return getActivity().getApplicationContext();
    }

}