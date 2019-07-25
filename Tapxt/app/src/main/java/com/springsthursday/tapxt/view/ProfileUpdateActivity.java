package com.springsthursday.tapxt.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.constract.ProfileUpdateContract;
import com.springsthursday.tapxt.databinding.ActivityProfileupdateBinding;
import com.springsthursday.tapxt.presenter.ProfileUpdatePresenter;

public class ProfileUpdateActivity extends AppCompatActivity implements ProfileUpdateContract.View {
    private Toolbar toolbar;
    private ProfileUpdatePresenter viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setNavigationBarColor(getColor(R.color.background));

        setContentView(R.layout.activity_profileupdate);

        setUpView();
    }

    public void setUpView()
    {
        viewModel = new ProfileUpdatePresenter(this);

        ActivityProfileupdateBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_profileupdate);
        binding.setViewModel(viewModel);

        Intent intent = getIntent();
        viewModel.setUserInfo(intent.getStringExtra("nickName"), intent.getStringExtra("imageUrl"));

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("프로필 수정");
        toolbar.getBackground().setAlpha(0);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
            case R.id.complete :
                viewModel.updateUserProfile();
                break;
        }

        return true;
    }

    public void finishActivity(String nickName, String imageUrl)
    {
        Intent intent = new Intent();

        intent.putExtra("nickName", nickName);
        intent.putExtra("imageUrl", imageUrl);

        setResult(RESULT_OK,intent);

        finish();
    }

    @Override
    public void showDuplicatedNickNameMessage() {
        Toast.makeText(this, "현재 닉네임과 동일합니다.", Toast.LENGTH_LONG).show();
    }
}
