package com.springsthursday.tapxt.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.constract.ContentContract;
import com.springsthursday.tapxt.databinding.ActivityContentBinding;
import com.springsthursday.tapxt.presenter.ContentPresenter;

public class ContentActivity extends AppCompatActivity implements ContentContract.View {

    private ContentPresenter viewModel;
    private ActivityContentBinding binding;
    private Toolbar toolbar;
    private String contentID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        this.setContentView(R.layout.activity_content);

        Intent intent = getIntent();
        contentID = intent.getStringExtra("EpisodeID");

        this.setUpView();
    }

    public void setUpView()
    {
        viewModel = new ContentPresenter(getApplicationContext(), contentID);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_content);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        viewModel.setRecyclerView(binding.recyclerView);
        viewModel.setAppBar(binding.appbar);
        viewModel.setImageView(binding.imageView);
        viewModel.setLottie(binding.like);
        viewModel.setLoderLottie(binding.animationView);

        toolbar = findViewById(R.id.toolbar);
        toolbar.getBackground().setAlpha(20);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left_darkerviolet);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator(){
            @Override
            public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
                return false;
            }
        });

        viewModel.inqueryContent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_appbar_content, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            case R.id.comments:
                Intent intent = new Intent(this, CommentActivity.class);
                intent.putExtra("EpisodeID", contentID);
                startActivity(intent);
                break;
        }
        return true;
    }
}
