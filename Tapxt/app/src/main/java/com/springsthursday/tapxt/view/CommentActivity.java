package com.springsthursday.tapxt.view;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.springsthursday.tapxt.BindingAdapter.CommentAdapter;
import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.constract.CommentContract;
import com.springsthursday.tapxt.databinding.ActivityCommentBinding;
import com.springsthursday.tapxt.item.CommentItem;
import com.springsthursday.tapxt.presenter.CommentPresenter;
import com.springsthursday.tapxt.repository.CommentRepository;
import com.springsthursday.tapxt.util.NetWorkBrodcastReceiver;

public class CommentActivity extends AppCompatActivity implements CommentContract.View {

    private CommentPresenter viewModel;
    private ActivityCommentBinding binding;
    private Toolbar toolbar;
    private String episodeID;
    private final int EDIT_COMMENT_REQUEST_CODE = 1;
    private Dialog dialog = null;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(getColor(R.color.background));
        window.setStatusBarColor(getColor(R.color.titlebar));

        this.setContentView(R.layout.activity_comment);

        Intent intent = getIntent();
        episodeID = intent.getStringExtra("EpisodeID");

        if (!NetWorkBrodcastReceiver.getInstance(getApplicationContext()).isOnline())
            Toast.makeText(getApplicationContext(), "네트워크 상태가 불안정합니다",Toast.LENGTH_LONG).show();

        this.setUpView();
    }

    public void setUpView()
    {
        viewModel = new CommentPresenter(this,getApplicationContext());

        binding = DataBindingUtil.setContentView(this,R.layout.activity_comment);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(manager);

        viewModel.setEpisodeID(episodeID);
        viewModel.setLoader(binding.animationView);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);

        viewModel.loadCommentList();
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

    @Override
    public void openCommentUpdateActivity(CommentItem item)
    {
        Intent intent = new Intent(getApplicationContext(), CommentUpdateActivity.class);
        intent.putExtra("Comment",item);

        this.startActivityForResult(intent, EDIT_COMMENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent)
    {
        if(EDIT_COMMENT_REQUEST_CODE == requestCode)
        {
            if(resultCode == RESULT_OK)
            {
                String comment = resultIntent.getStringExtra("Comment");
                String commentID = resultIntent.getStringExtra("CommentID");

                ((CommentAdapter)binding.recyclerView.getAdapter()).updateComment(comment, commentID);
            }
        }
    }
    @Override
    public void showProgressDialog(String loadingMessage) {

        if(dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.dialog_progressbar, null);
            ((TextView)view.findViewById(R.id.loadingMessage)).setText(loadingMessage);
            builder.setView(view);

            dialog = builder.create();
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            dialog.show();
        }
    }

    @Override
    public void hideProgressDialog() {
        if(dialog != null) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            dialog.dismiss();
            dialog = null;
        }
    }

    public void hideKeybord() {
        InputMethodManager imm = (InputMethodManager) getSystemService(getApplication().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.inputComment.getWindowToken(), 0);
    }
}
