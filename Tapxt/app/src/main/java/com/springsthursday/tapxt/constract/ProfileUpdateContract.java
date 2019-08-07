package com.springsthursday.tapxt.constract;

import android.net.Uri;

public interface ProfileUpdateContract {
    interface View
    {
        void finishActivity(String nickName, String imageUrl);
        void showDuplicatedNickNameMessage();
        void confirmPermission();
        String getRealPath(Uri uri);
    }
}
