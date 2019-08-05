package com.springsthursday.tapxt.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.constract.VerificationContract;
import com.springsthursday.tapxt.databinding.ActivityVerificationBinding;
import com.springsthursday.tapxt.presenter.VerificationPresenter;

public class VerificationActivity extends AppCompatActivity implements VerificationContract.View {

    private String phoneNumber;
    private VerificationPresenter verificationPresenter;
    private ActivityVerificationBinding binding;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setNavigationBarColor(getColor(R.color.background));

        setContentView(R.layout.activity_verification);

        this.setUpView();
    }

    public void setUpView()
    {
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("phoneNumber");

        verificationPresenter = new VerificationPresenter(this, phoneNumber);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_verification);
        binding.setViewModel(verificationPresenter);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void MoveMainContentActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void showExceptionErrorMessage() {
        Toast.makeText(getApplicationContext(), "예기치 못한 오류가 발생했습니다. 잠시 후 다시 시도해주세요", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showResponseServerErrorMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showInputVerificationNumberMessage() {
        Toast.makeText(getApplicationContext(), "인증번호를 입력하세요", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        verificationPresenter.dispose();
    }

    @Override
    public void hideKeybord() {
        InputMethodManager imm = (InputMethodManager) getSystemService(getApplication().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.verificationNumber.getWindowToken(), 0);
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
}
