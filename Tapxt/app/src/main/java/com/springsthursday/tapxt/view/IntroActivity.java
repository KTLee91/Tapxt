package com.springsthursday.tapxt.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.constract.IntroContract;
import com.springsthursday.tapxt.databinding.ActivityIntroBinding;
import com.springsthursday.tapxt.handler.UserInfoHandler;
import com.springsthursday.tapxt.item.UserInfoItem;
import com.springsthursday.tapxt.database.DatabaseManager;
import com.springsthursday.tapxt.presenter.ContentPresenter;
import com.springsthursday.tapxt.presenter.IntroPresenter;
import com.springsthursday.tapxt.repository.AppInfoRepository;
import com.springsthursday.tapxt.repository.AppSettingIngo;
import com.springsthursday.tapxt.util.NetWorkBrodcastReceiver;

public class IntroActivity extends AppCompatActivity implements IntroContract.View {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private IntroPresenter viewModel;
    private ActivityIntroBinding binding;
    private String device_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Window window = getWindow();
        getWindow().setNavigationBarColor(getColor(R.color.background));
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        setContentView(R.layout.activity_intro);

        if (!NetWorkBrodcastReceiver.getInstance(getApplicationContext()).isOnline())
            showNetworkFailDialog();

        try {
            device_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            AppInfoRepository.getInstance().setVersion(device_version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        DatabaseManager.getInstance(getApplicationContext()).displayRecord();

        viewModel = new IntroPresenter(getApplicationContext(), this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        AppSettingIngo.getInstance().setAutoSpeed(preferences.getInt("AutoSpeed", 1000));

        viewModel.checkVersion(device_version);
    }

    @Override
    public void contextManinActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void contextLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void showNetworkFailDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("네트워크 오류").setMessage("네트워크 상태가 불안정합니다");
        builder.setCancelable(true);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void confirmVersionDialog(boolean isMajar) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("업데이트").setMessage("최신 버전의 어플리케이션이 있습니다");
        builder.setCancelable(true);

        builder.setPositiveButton("업데이트", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                finish();
            }
        });

        if(isMajar == false) {
            builder.setNegativeButton("다음에 할게요", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    viewModel.loadData();
                }
            });
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(viewModel.disposable != null)
            viewModel.disposable.dispose();
    }
}
