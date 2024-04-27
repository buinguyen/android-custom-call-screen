package com.nguyenbnt.android.ui;

import android.content.Context;

public interface CallScreen {
    void showScreen(final Context context,
                    final String contactName,
                    final String phoneNumber);

    void closeScreen(final Context context);
}
