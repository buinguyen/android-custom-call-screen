package com.nguyenbnt.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.nguyenbnt.android.contact.ContactMappingHandler;
import com.nguyenbnt.android.contact.TodoContactMappingHandler;
import com.nguyenbnt.android.ui.CallScreen;
import com.nguyenbnt.android.ui.OverlaysCallScreen;

public class IncomingCallReceiver extends BroadcastReceiver {
    private static final String TAG = "IncomingCallReceiver";

    private static final String ACTION_PHONE_STATE = "android.intent.action.PHONE_STATE";

    private final CallScreen callScreen = new OverlaysCallScreen();
    private final ContactMappingHandler contactMappingHandler = new TodoContactMappingHandler();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_PHONE_STATE.equals(intent.getAction())) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.d(TAG, "phone state: " + state + ", incoming number: " + number);

            if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
                /* Show custom dialing screen if the state is ringing */
                if (number != null) {
                    String contactName = contactMappingHandler.getContactName(number);
                    callScreen.showScreen(context, contactName, number);
                }
            } else if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)
                    || TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {
                /* Hide custom dialing screen if the call is ended or connected */
                callScreen.closeScreen(context);
            }
        }
    }

}