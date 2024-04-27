package com.nguyenbnt.android.ui;

import android.content.Context;

import com.nguyenbnt.android.ui.CallScreen;
import com.nguyenbnt.android.ui.CallWindowManager;

public class OverlaysCallScreen implements CallScreen {

    @Override
    public void showScreen(Context context, String contactName, String phoneNumber) {
        CallWindowManager.getInstance().showWindow(context, contactName, phoneNumber);
    }

    @Override
    public void closeScreen(Context context) {
        CallWindowManager.getInstance().closeWindow(context);
    }
}
