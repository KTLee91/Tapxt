package com.springsthursday.tapxt.constract;

public interface LoginContract {
    interface View{
        void MoveVerificationActivity(String phoneNumber);
        void showExceptionErrorMessage();
        void showResponseServerErrorMessage(String message);
        void showInvalidPhoneNumberMessage();
        void hideKeybord();
        void showProgressDialog(String loadingMessage);
        void hideProgressDialog();
    }
}
