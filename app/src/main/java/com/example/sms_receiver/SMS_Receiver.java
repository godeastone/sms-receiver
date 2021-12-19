package com.example.sms_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SMS_Receiver extends BroadcastReceiver {

    private static final String TAG = "In SMS_Receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive() called");

        String number = "";
        Bundle bundle = intent.getExtras();
        //parsing message
        SmsMessage[] messages = parseMessage(bundle);

        if(messages.length > 0) {
            String sender = messages[0].getOriginatingAddress();
            String content = messages[0].getMessageBody().toString();
            Date date = new Date(messages[0].getTimestampMillis());

            //extract numbers in SMS message
            number = content.replaceAll("[^0-9]", "");

            //for Debug
            Log.d(TAG, "sender : " + sender);
            Log.d(TAG, "content : " + content);
            Log.d(TAG, "date : " + date);

            //send number to activity
            sendToActivity(context, number);
        }
    }

    private SmsMessage[] parseMessage(Bundle bundle) {
        Log.d(TAG, "parseSmsMessage() called");

        Object[] objs = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[objs.length];

        for(int i = 0; i < objs.length; i++) {
            messages[i] = SmsMessage.createFromPdu((byte[])objs[i]);
        }

        return messages;
    }

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private void sendToActivity(Context context, String number){
        Log.d(TAG, "sendToActivity() called");

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                |Intent.FLAG_ACTIVITY_SINGLE_TOP
                |Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("number", number);

        context.startActivity(intent);
    }
}