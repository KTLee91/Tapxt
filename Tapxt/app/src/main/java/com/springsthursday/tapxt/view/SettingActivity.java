package com.springsthursday.tapxt.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.constract.SettingContract;
import com.springsthursday.tapxt.databinding.ActivitySettingBinding;
import com.springsthursday.tapxt.presenter.SettingPresenter;

public class SettingActivity extends AppCompatActivity implements SettingContract.View {

    private SettingPresenter viewModel;
    private ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();

        setContentView(R.layout.activity_profileupdate);

        window.setNavigationBarColor(getColor(R.color.background));
        window.setStatusBarColor(getColor(R.color.titlebar));

        setUpView();
    }

    public void setUpView()
    {
        viewModel = new SettingPresenter(this, getApplicationContext());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        binding.setViewModel(viewModel);

        binding.toolbar.setTitle("프로필 수정");
        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    @Override
    public void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
