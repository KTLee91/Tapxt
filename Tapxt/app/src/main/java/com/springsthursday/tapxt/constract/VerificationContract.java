package com.springsthursday.tapxt.constract;

import android.content.Context;

public interface VerificationContract {

    interface View{
        Context getContext();
        void MoveMainContentActivity();
        void showExceptionErrorMessage();
        void showResponseServerErrorMessage(String message);
        void showInputVerificationNumberMessage();
        void hideKeybord();
        void showProgressDialog(String loadingMessage);
        void hideProgressDialog();
    }
}
