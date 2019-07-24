package com.springsthursday.tapxt.view;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.OnCompositionLoadedListener;
import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.handler.UserInfoHandler;
import com.springsthursday.tapxt.item.UserInfoItem;
import com.springsthursday.tapxt.database.DatabaseManager;

public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        setContentView(R.layout.activity_intro);

        DatabaseManager.getInstance(getApplicationContext()).displayRecord();

        LottieAnimationView animationView = findViewById(R.id.animation_view);

        LottieDrawable drawable = new LottieDrawable();

        LottieComposition.Factory.fromAssetFileName(this, "intro.json",(composition -> {
            drawable.setComposition(composition);
            drawable.playAnimation();
            drawable.setScale(3);
            animationView.setImageDrawable(drawable);
        }));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Cursor cursor =  DatabaseManager.getInstance(getApplicationContext()).getVerificationUser();
                if(cursor.getCount() > 0)
                {
                    while(cursor.moveToNext())
                    {
                        UserInfoItem userInfoItem = new UserInfoItem();
                        userInfoItem.setPhoneNumber(cursor.getString(1));
                        userInfoItem.setToken(cursor.getString(2));

                        UserInfoHandler.getInstance().setUserInfo(userInfoItem);
                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 5000);
    }
}
