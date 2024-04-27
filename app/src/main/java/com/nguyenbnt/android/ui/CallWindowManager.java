package com.nguyenbnt.android.ui;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.nguyenbnt.android.R;

/**
 * Defines as a singleton object because many views were created.
 * It may be the reason can not close window correctly.
 */
public class CallWindowManager {
    private static final String TAG = "CallWindowManager";

    private static final float WINDOW_WIDTH_RATIO = 0.8f;

    private static final CallWindowManager instance = new CallWindowManager();

    private ViewGroup windowLayout;

    private WindowManager.LayoutParams params;

    private float x;
    private float y;

    private CallWindowManager() {
        // Do nothing
    }

    public static CallWindowManager getInstance() {
        return instance;
    }

    public void showWindow(final Context context,
                           final String contactName,
                           final String phoneNumber) {
        final WindowManager wm = getWindowManager(context);
        if (wm == null) {
            Log.e(TAG, "show window failed");
            return;
        }
        if (windowLayout == null) {
            windowLayout = (ViewGroup) View.inflate(context, R.layout.layout_call, null);
            getLayoutParams(wm);
            setOnTouchListener(wm);

            TextView contactTextView = windowLayout.findViewById(R.id.tv_contact);
            contactTextView.setText(contactName);
            TextView numberTextView = windowLayout.findViewById(R.id.tv_number);
            numberTextView.setText(phoneNumber);
            Button cancelButton = windowLayout.findViewById(R.id.btn_close);
            cancelButton.setOnClickListener(view -> closeWindow(context));

            wm.addView(windowLayout, params);
        }
    }

    public void closeWindow(final Context context) {
        WindowManager wm = getWindowManager(context);
        if (wm == null) {
            Log.d(TAG, "close window ignored");
            return;
        }
        if (windowLayout != null) {
            wm.removeView(windowLayout);
            windowLayout = null;
            Log.d(TAG, "close window done");
        } else {
            Log.d(TAG, "close window ignored");
        }
    }

    @Nullable
    private WindowManager getWindowManager(Context context) {
        return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    private void getLayoutParams(final WindowManager windowManager) {
        int windowFlag = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                getWindowsTypeParameter(),
                windowFlag,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP;
        params.format = 1;
        params.width = getWindowWidth(windowManager);
    }

    private int getWindowsTypeParameter() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        return WindowManager.LayoutParams.TYPE_PHONE;
    }

    private int getWindowWidth(final WindowManager windowManager) {
        if (windowManager == null) {
            return 0;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return (int) (WINDOW_WIDTH_RATIO * (double) metrics.widthPixels);
    }

    private void setOnTouchListener(final WindowManager windowManager) {
        if (windowManager == null || windowLayout == null) {
            return;
        }
        windowLayout.setOnTouchListener((view, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = event.getRawX();
                    y = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    updateWindowLayoutParams(windowManager, event);
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        });
    }

    private void updateWindowLayoutParams(final WindowManager windowManager, MotionEvent event) {
        if (windowManager == null || params == null) {
            return;
        }
        params.x = params.x - (int) (x - event.getRawX());
        params.y = params.y - (int) (y - event.getRawY());
        windowManager.updateViewLayout(windowLayout, params);
        x = event.getRawX();
        y = event.getRawY();
    }

}
