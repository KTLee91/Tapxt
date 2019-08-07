package com.springsthursday.tapxt.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.databinding.ObservableField;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.rx2.Rx2Apollo;
import com.springsthursday.tapxt.EditUserMutation;
import com.springsthursday.tapxt.constract.ProfileUpdateContract;
import com.springsthursday.tapxt.repository.UserInfo;
import com.springsthursday.tapxt.util.ApolloClientObject;
import com.springsthursday.tapxt.util.UploadImageInterface;
import com.springsthursday.tapxt.util.UploadObject;
import com.springsthursday.tapxt.validation.Validation;
import com.springsthursday.tapxt.view.ProfileUpdateActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileUpdatePresenter {
    public ObservableField<String> imageUrl = new ObservableField<>();
    public ObservableField<String> editNickName = new ObservableField<>();

    private CompositeDisposable disposable;
    private ProfileUpdateContract.View activity;
    private Uri uri;
    private Context context;

    public ProfileUpdatePresenter(ProfileUpdateContract.View view, Context context) {
        this.activity = view;
        this.context = context;
    }

    public void setUserInfo(String nickName, String imageUrl) {
        this.editNickName.set(nickName);
        this.imageUrl.set(imageUrl);
    }

    public void setURI(Uri uri) {
        this.uri = uri;
    }

    public void updateUserProfile() {

        if(Validation.isEmptyNickName(editNickName.get()))
        {
            Toast.makeText(context,"닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        ApolloClient apolloClienmt = ApolloClientObject.getApolloClient();

        EditUserMutation mutation = EditUserMutation
                .builder()
                .avatar(imageUrl.get())
                .nickname(editNickName.get())
                .build();
        ApolloCall<EditUserMutation.Data> apolloCall1 = apolloClienmt.mutate(mutation);
        Observable<Response<EditUserMutation.Data>> observable = Rx2Apollo.from(apolloCall1);

        disposable = new CompositeDisposable();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<EditUserMutation.Data>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(Response<EditUserMutation.Data> dataResponse) {
                        UserInfo.getInstance().userInfoItem.setNickName(editNickName.get());
                        UserInfo.getInstance().userInfoItem.setImageUrl(imageUrl.get());

                        activity.finishActivity(editNickName.get(), imageUrl.get());
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void clickComplete(View view) {
        if (uri != null) {
            File file = new File(activity.getRealPath(uri));

            RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://tapxt-dev.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    //.client(client)
                    .build();

            UploadImageInterface uploadImage = retrofit.create(UploadImageInterface.class);
            Call<UploadObject> fileUpload = uploadImage.uploadFile(fileToUpload);
            fileUpload.enqueue(new Callback<UploadObject>() {
                @Override
                public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {
                    //updateUserProfile();
                    //imageUrl =
                    Log.d("Success", response.body().getKey());
                }

                @Override
                public void onFailure(Call<UploadObject> call, Throwable t) {
                    Log.d("Fail", "Failed File Upload");
                }
            });
        }
        else
        {
            this.updateUserProfile();
        }
    }

    public void imageClickHandler(View view) {
        activity.confirmPermission();
    }
}
