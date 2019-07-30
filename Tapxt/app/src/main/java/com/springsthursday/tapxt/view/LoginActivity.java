package com.springsthursday.tapxt.view;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.constract.LoginContract;
import com.springsthursday.tapxt.databinding.ActivityLoginBinding;
import com.springsthursday.tapxt.presenter.LoginPresenter;

public class LoginActivity extends AppCompatActivity implements LoginContract.View{

    public EditText editTextPhoneNumber = null;
    public Button buttonLogin = null;
    private ActivityLoginBinding loginBinding = null;
    private LoginPresenter loginPresenter = null;
    private Dialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        getWindow().setNavigationBarColor(getColor(R.color.background));
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        setContentView(R.layout.activity_login);
        this.setUpView();
    }

    private void setUpView(){

        loginPresenter = new LoginPresenter(this, getApplicationContext());

        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginBinding.setViewModel(loginPresenter);

        editTextPhoneNumber = findViewById(R.id.phoneNumber);
        buttonLogin = findViewById(R.id.login);
    }

    @Override
    public void MoveVerificationActivity(String phoneNumber) {
        Intent intent = new Intent(this, VerificationActivity.class);

        intent.putExtra("phoneNumber", phoneNumber);
        startActivity(intent);

        loginPresenter.dispose();
    }

    public void showInvalidPhoneNumberMessage()
    {
        Toast.makeText(getApplicationContext(), "올바르지 않은 번호입니다", Toast.LENGTH_LONG).show();
    }
    public void showResponseServerErrorMessage(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
    public void showExceptionErrorMessage()
    {
        Toast.makeText(getApplicationContext(), "예기치 못한 오류가 발생했습니다. 잠시 후 다시 시도해주세요", Toast.LENGTH_LONG).show();
    }

    @Override
    public void hideKeybord() {
        InputMethodManager imm = (InputMethodManager) getSystemService(getApplication().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(loginBinding.phoneNumber.getWindowToken(), 0);
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
