package com.springsthursday.tapxt.view;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.constract.CommentUpdateContract;
import com.springsthursday.tapxt.databinding.ActivityUpdatecommentBinding;
import com.springsthursday.tapxt.item.CommentItem;
import com.springsthursday.tapxt.presenter.CommentUpdatePresenter;

public class CommentUpdateActivity extends AppCompatActivity implements CommentUpdateContract.View {

    private Toolbar toolbar;
    private CommentUpdatePresenter viewModel;
    private CommentItem item;
    private ActivityUpdatecommentBinding binding;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(getResources().getColor(R.color.titlebar, null));
        getWindow().setNavigationBarColor(getColor(R.color.background));

        setContentView(R.layout.activity_updatecomment);

        Intent intent = getIntent();
        item = (CommentItem)intent.getSerializableExtra("Comment");

        setUpView();
    }

    public void setUpView()
    {
        viewModel = new CommentUpdatePresenter(this,getApplicationContext(), item);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_updatecomment);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
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

    @Override
    public void showProgressDialog(String message) {
        if(dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.dialog_progressbar, null);
            ((TextView)view.findViewById(R.id.loadingMessage)).setText(message);
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

    @Override
    public void hideKeybord() {
        InputMethodManager imm = (InputMethodManager) getSystemService(getApplication().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.editText2.getWindowToken(), 0);
    }
}
