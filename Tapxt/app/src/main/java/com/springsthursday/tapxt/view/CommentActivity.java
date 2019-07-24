package com.springsthursday.tapxt.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.constract.CommentContract;
import com.springsthursday.tapxt.databinding.ActivityCommentBinding;
import com.springsthursday.tapxt.item.CommentItem;
import com.springsthursday.tapxt.presenter.CommentPresenter;
import com.springsthursday.tapxt.repository.CommentRepository;

public class CommentActivity extends AppCompatActivity implements CommentContract.View {

    private CommentPresenter viewModel;
    private ActivityCommentBinding binding;
    private Toolbar toolbar;
    private String episodeID;
    private final int EDIT_COMMENT_REQUEST_CODE = 1;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_comment);

        Intent intent = getIntent();
        episodeID = intent.getStringExtra("EpisodeID");

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

        viewModel.loadData();
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

                CommentRepository.getInstance().updateComment(comment, commentID);
                binding.recyclerView.getAdapter().notifyDataSetChanged();
            }
        }
    }
}
