package com.springsthursday.tapxt.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.constract.ContentCoverContract;
import com.springsthursday.tapxt.databinding.ActivityContentcoverBinding;
import com.springsthursday.tapxt.item.StoryItem;
import com.springsthursday.tapxt.presenter.ContentCoverPresenter;

public class ContentCoverActivity extends AppCompatActivity implements ContentCoverContract.View {

    private Toolbar toolbar;
    private ContentCoverPresenter viewModel;
    private String storyTitle;
    private ActivityContentcoverBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        this.setContentView(R.layout.activity_contentcover);

        Intent intent = getIntent();

        storyTitle  = intent.getStringExtra("Story");

        this.setUpView();
    }

    public void setUpView()
    {
        viewModel = new ContentCoverPresenter(this, storyTitle, getApplication());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_contentcover);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setSupportActionBar(binding.toolbar);

        viewModel.setLoderLottie(binding.loader);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);

        binding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0)
                {
                    binding.appbarTitle.setVisibility(View.VISIBLE);
                }
                else
                {
                    binding.appbarTitle.setVisibility(View.GONE);
                }
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setHasFixedSize(true);

        viewModel.loadStoryItem();
    }

    @Override
    public void openContentActivity(String contentId) {
        Intent intent = new Intent(this, ContentActivity.class);
        intent.putExtra("EpisodeID", contentId);
        startActivity(intent);
    }

    @Override
    public void openCommentActivity(String episodeId) {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra("EpisodeID", episodeId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                finish();
        }

        return true;
    }
}
