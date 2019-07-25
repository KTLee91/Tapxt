package com.springsthursday.tapxt.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.springsthursday.tapxt.Code.Code;
import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.constract.MainContract;
import com.springsthursday.tapxt.databinding.ActivityMainBinding;
import com.springsthursday.tapxt.presenter.MainPresenter;
import com.springsthursday.tapxt.repository.StoryRepository;
import com.springsthursday.tapxt.repository.UserInfo;

public class MainActivity extends AppCompatActivity implements MainContract.View{

    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private CategoryFragment categoryFragment;
    private ProfileFragment profileFragment;
    private FragmentTransaction transaction;
    private MainPresenter viewModel;
    private Toolbar toolbar;
    private static final int UPDATE_PROFILE_REQUEST_CODE = 1;
    private LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getWindow().setNavigationBarColor(getColor(R.color.background));

        setContentView(R.layout.activity_main);

        this.setUpView();
    }

    public void setUpView() {
        viewModel = new MainPresenter(this);
        fragmentManager = getSupportFragmentManager();

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(viewModel);

        homeFragment = new HomeFragment();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frameLayout, homeFragment).commitAllowingStateLoss();
    }

    @Override
    public void changeFragment(String fragment) {
        transaction = fragmentManager.beginTransaction();

        switch(fragment)
        {
            case Code.FragmentName.FGAGMENT_HOME:
                if(homeFragment == null)
                {
                    homeFragment = new HomeFragment();
                    fragmentManager.beginTransaction().add(R.id.frameLayout, homeFragment).commit();
                }
                Window window = getWindow();

                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setNavigationBarColor(getColor(R.color.background));

                fragmentManager.beginTransaction().show(homeFragment).commit();

                if(categoryFragment != null) fragmentManager.beginTransaction().hide(categoryFragment).commit();
                if(profileFragment != null) fragmentManager.beginTransaction().hide(profileFragment).commit();

                break;
            case Code.FragmentName.FRAGMENT_CATEGORY:
                if(categoryFragment == null)
                {
                    categoryFragment = new CategoryFragment();
                    fragmentManager.beginTransaction().add(R.id.frameLayout, categoryFragment).commit();
                }

                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().setNavigationBarColor(getColor(R.color.background));

                fragmentManager.beginTransaction().show(categoryFragment).commit();
                if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
                if(profileFragment != null) fragmentManager.beginTransaction().hide(profileFragment).commit();
                break;
            case Code.FragmentName.FRAGMENT_PROFILE:
                if(profileFragment == null)
                {
                    profileFragment = new ProfileFragment();
                    fragmentManager.beginTransaction().add(R.id.frameLayout, profileFragment).commit();
                }

                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().setNavigationBarColor(getColor(R.color.background));

                fragmentManager.beginTransaction().show(profileFragment).commit();
                if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
                if(categoryFragment != null) fragmentManager.beginTransaction().hide(categoryFragment).commit();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.setting:
                Intent intent = new Intent(this, ProfileUpdateActivity.class);

                intent.putExtra("nickName", UserInfo.getInstance().userInfoItem.getNickName());
                intent.putExtra("imageUrl", UserInfo.getInstance().userInfoItem.getImageUrl());

                startActivityForResult(intent, UPDATE_PROFILE_REQUEST_CODE);
                break;
        }

        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent)
    {
        if(UPDATE_PROFILE_REQUEST_CODE == requestCode)
        {
            if(resultCode == RESULT_OK)
            {
                profileFragment.setUserInfo(
                    resultIntent.getStringExtra("nickName"),
                    resultIntent.getStringExtra("imageUrl")
                );
            }
        }
    }
}
