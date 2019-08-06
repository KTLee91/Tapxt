package com.springsthursday.tapxt.constract;

public interface ProfileUpdateContract {
    interface View
    {
        void finishActivity(String nickName, String imageUrl);
        void showDuplicatedNickNameMessage();
        void openGallery();
    }
}
