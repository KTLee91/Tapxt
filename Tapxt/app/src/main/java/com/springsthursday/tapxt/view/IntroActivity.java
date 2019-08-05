package com.springsthursday.tapxt.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.constract.IntroContract;
import com.springsthursday.tapxt.databinding.ActivityIntroBinding;
import com.springsthursday.tapxt.handler.UserInfoHandler;
import com.springsthursday.tapxt.item.UserInfoItem;
import com.springsthursday.tapxt.database.DatabaseManager;
import com.springsthursday.tapxt.presenter.ContentPresenter;
import com.springsthursday.tapxt.presenter.IntroPresenter;
import com.springsthursday.tapxt.repository.AppSettingIngo;

public class IntroActivity extends AppCompatActivity implements IntroContract.View {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private IntroPresenter viewModel;
    private ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        setContentView(R.layout.activity_intro);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        DatabaseManager.getInstance(getApplicationContext()).displayRecord();

        viewModel = new IntroPresenter(getApplicationContext(), this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        AppSettingIngo.getInstance().setAutoSpeed(preferences.getInt("AutoSpeed", 1000));

        viewModel.loadData();
    }

    @Override
    public void contextManinActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void contextLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
