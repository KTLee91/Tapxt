package com.springsthursday.tapxt.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.constract.ProfileUpdateContract;
import com.springsthursday.tapxt.databinding.ActivityProfileupdateBinding;
import com.springsthursday.tapxt.presenter.ProfileUpdatePresenter;
import com.springsthursday.tapxt.util.NetworkClient;
import com.springsthursday.tapxt.util.UploadImageInterface;
import com.springsthursday.tapxt.util.UploadObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileUpdateActivity extends AppCompatActivity implements ProfileUpdateContract.View{
    private Toolbar toolbar;
    private ProfileUpdatePresenter viewModel;
    private static int REQUEST_IMAGE = 0;
    private ActivityProfileupdateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setNavigationBarColor(getColor(R.color.background));
        getWindow().setStatusBarColor(getColor(R.color.titlebar));

        setContentView(R.layout.activity_profileupdate);

        setUpView();
    }

    public void setUpView() {
        viewModel = new ProfileUpdatePresenter(this, getApplicationContext());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profileupdate);
        binding.setViewModel(viewModel);

        Intent intent = getIntent();
        viewModel.setUserInfo(intent.getStringExtra("nickName"), intent.getStringExtra("imageUrl"));

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("프로필 수정");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
        }
        return true;
    }

    public void finishActivity(String nickName, String imageUrl) {
        Intent intent = new Intent();

        intent.putExtra("nickName", nickName);
        intent.putExtra("imageUrl", imageUrl);

        setResult(RESULT_OK, intent);

        finish();
    }

    @Override
    public void showDuplicatedNickNameMessage() {
        Toast.makeText(this, "현재 닉네임과 동일합니다.", Toast.LENGTH_LONG).show();
    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap img = null;
                viewModel.setURI(data.getData());
                InputStream in = null;
                try {
                    in = getContentResolver().openInputStream(data.getData());
                    img = BitmapFactory.decodeStream(in);
                    in.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(binding.image.getContext())
                        .load(img)
                        .apply(new RequestOptions().circleCrop()).into(binding.image);
            }
        }
    }

    @Override
    public void confirmPermission() {
        if(EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            openGallery();
        }
        else
        {
            EasyPermissions.requestPermissions(this, getString(R.string.read_file), 300, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public String getRealPath(Uri uri) {
            String result = "";
            Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
            if (cursor == null) {
                result = uri.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
            }
        return result;
    }
}
