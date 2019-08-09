package com.springsthursday.tapxt.view;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.constract.ContentContract;
import com.springsthursday.tapxt.databinding.ActivityContentBinding;
import com.springsthursday.tapxt.databinding.DialogEpisodeBinding;
import com.springsthursday.tapxt.item.AppSettingItem;
import com.springsthursday.tapxt.listener.RecyclerTouchListener;
import com.springsthursday.tapxt.listener.RecyclerViewClickListener;
import com.springsthursday.tapxt.presenter.ContentPresenter;
import com.springsthursday.tapxt.repository.AppSettingIngo;
import com.springsthursday.tapxt.util.NetWorkBrodcastReceiver;

import java.util.Timer;
import java.util.TimerTask;

public class ContentActivity extends AppCompatActivity implements ContentContract.View {

    private ContentPresenter viewModel;
    private ActivityContentBinding binding;
    private Toolbar toolbar;
    private String contentID;
    private Dialog episodeDialog;
    private Timer timer;
    private TimerTask timerTask;
    private boolean isAutoLoadContent = false;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(getColor(R.color.titlebar));
        getWindow().setNavigationBarColor(getColor(R.color.background));

        this.setContentView(R.layout.activity_content);

        Intent intent = getIntent();
        contentID = intent.getStringExtra("EpisodeID");

        Log.d("EpisodeID", contentID);

        if (!NetWorkBrodcastReceiver.getInstance(getApplicationContext()).isOnline())
            Toast.makeText(getApplicationContext(), "네트워크 상태가 불안정합니다",Toast.LENGTH_LONG).show();

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

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        binding.autoLoadSpeed.setProgress(AppSettingIngo.getInstance().getautoSpeed());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
                return false;
            }
        });

        ((DefaultItemAnimator) binding.recyclerView.getItemAnimator()).setAddDuration(0);
        ((DefaultItemAnimator) binding.recyclerView.getItemAnimator()).setRemoveDuration(500);

        binding.autoLoadSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int period = seekBar.getMax() - seekBar.getProgress();

                editor.putInt("AutoSpeed", seekBar.getProgress());
                editor.apply();

                AppSettingIngo.getInstance().setAutoSpeed(seekBar.getProgress());

                autoLoadContentStop(false);
                autoLoadContent(period);
            }
        });

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

        if(timer != null) timer.cancel();
        if(episodeDialog != null) episodeDialog.dismiss();
        if(viewModel.disposable != null) viewModel.disposable.dispose();

        if(viewModel.backgroundSoundPlayer != null) {
            viewModel.backgroundSoundPlayer.release();
            viewModel.backgroundSoundPlayer = null;
        }
    }

    @Override
    protected void onUserLeaveHint() {
        if(viewModel.backgroundSoundPlayer != null) {
            if(viewModel.backgroundSoundPlayer.isPlaying() == true)
            {
                try {
                    viewModel.backgroundSoundPlayer.stop();
                    viewModel.backgroundSoundPlayer.release();
                    viewModel.backgroundSoundPlayer=null;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        }
        super.onUserLeaveHint();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        viewModel.startBackgroundSound();

    }

    @Override
    public void setTouchListener() {
        binding.recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), binding.recyclerView, new clickListener()));
    }

    public class clickListener implements RecyclerViewClickListener
    {
        @Override
        public void onClick() {
            viewModel.loadContent();
        }

        @Override
        public void onLongClick() {
            setTimer(true, false, binding.autoLoadSpeed.getMax() - AppSettingIngo.getInstance().getautoSpeed());
        }
    }

    @Override
    public void setTimer(boolean isLongClick, boolean isAutoSpeedChanged, int period) {
        if (isAutoLoadContent == false) {
            isAutoLoadContent = true;
            autoLoadContent(period);
        } else {
            if(isLongClick == false) {
                isAutoLoadContent = false;
                autoLoadContentStop(isAutoSpeedChanged);
            }
        }
    }

    public void autoLoadContent(int period) {

        viewModel.setAutoLoadViewVisibility(View.VISIBLE);

        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewModel.loadContent();
                    }
                });
            }
        }, 0, period * 100);
    }

    public void autoLoadContentStop(boolean isAutoSpeedChanged) {
        timer.cancel();

        if(isAutoSpeedChanged == false)
            viewModel.setAutoLoadViewVisibility(View.GONE);
    }

    public void autoLoadContentStop()
    {
        if(isAutoLoadContent == true) {
            timer.cancel();
            viewModel.setAutoLoadViewVisibility(View.GONE);
        }
    }
}
