package com.springsthursday.tapxt.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.constract.CommentUpdateContract;
import com.springsthursday.tapxt.databinding.ActivityUpdatecommentBinding;
import com.springsthursday.tapxt.item.CommentItem;
import com.springsthursday.tapxt.presenter.CommentUpdatePresenter;

public class CommentUpdateActivity extends AppCompatActivity implements CommentUpdateContract.View {

    private Toolbar toolbar;
    private CommentUpdatePresenter viewModel;
    private CommentItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatecomment);

        Intent intent = getIntent();
        item = (CommentItem)intent.getSerializableExtra("Comment");

        setUpView();
    }

    public void setUpView()
    {
        viewModel = new CommentUpdatePresenter(this,getApplicationContext(), item);

        ActivityUpdatecommentBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_updatecomment);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        viewModel.setLottie(binding.animationView);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("댓글 수정");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left_black);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_appbar_profileupdate, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
            case R.id.complete:
                viewModel.editComment();
        }

        return true;
    }

    @Override
    public void finishActivity(String comment, String commentID) {
        Intent intent = new Intent();
        intent.putExtra("Comment", comment);
        intent.putExtra("CommentID", commentID);

        setResult(RESULT_OK,intent);
        finish();
    }
}
