package com.springsthursday.tapxt.view;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.constract.ContentContract;
import com.springsthursday.tapxt.databinding.ActivityContentBinding;
import com.springsthursday.tapxt.databinding.DialogEpisodeBinding;
import com.springsthursday.tapxt.presenter.ContentPresenter;

public class ContentActivity extends AppCompatActivity implements ContentContract.View {

    private ContentPresenter viewModel;
    private ActivityContentBinding binding;
    private Toolbar toolbar;
    private String contentID;
    private Dialog episodeDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(getColor(R.color.titlebar));
        getWindow().setNavigationBarColor(getColor(R.color.background));

        this.setContentView(R.layout.activity_content);

        Intent intent = getIntent();
        contentID = intent.getStringExtra("EpisodeID");

        this.setUpView();
    }

    public void setUpView() {
        viewModel = new ContentPresenter(getApplicationContext(), this, contentID);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_content);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        viewModel.setRecyclerView(binding.recyclerView);
        viewModel.setViewFlipper(binding.viewFlipper);
        viewModel.setLoderLottie(binding.animationView);
        viewModel.setAppbar(binding.appbar);
        viewModel.setLikeToggle(binding.like);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
                return false;

            }
        });


        binding.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in));
        binding.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out));

        viewModel.inqueryContent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appbar_content, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

    @Override
    public void showEpisodeDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogEpisodeBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(getApplicationContext()), R.layout.dialog_episode, null, false);
        dialogBinding.setViewModel(viewModel);
        dialogBinding.setLifecycleOwner(this);

        builder.setView(dialogBinding.getRoot());

        episodeDialog = builder.create();
        episodeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        episodeDialog.show();
    }

    @Override
    public void openNextBtn(String nextEpisodeID) {
        Intent intent = new Intent(this, ContentActivity.class);
        intent.putExtra("EpisodeID", nextEpisodeID);
        startActivity(intent);
        if(episodeDialog != null) episodeDialog.dismiss();
        finish();
    }

    @Override
    public void openEpisodeList() {
        finish();
    }

    @Override
    public void openCommentList() {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra("EpisodeID", contentID);
        startActivity(intent);
    }

    @Override
    public void closeDialog() {
        if(episodeDialog != null)
            episodeDialog.dismiss();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if(episodeDialog != null) episodeDialog.dismiss();
        if(viewModel.disposable != null) viewModel.disposable.dispose();
    }
}
