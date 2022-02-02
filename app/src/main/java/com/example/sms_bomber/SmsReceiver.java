package com.example.sms_bomber;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
    String message;
    String sender;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String str = "";

        //Envoie le message reçu dans un toast
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i=0; i<msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                str += "SMS de " + msgs[i].getOriginatingAddress();
                sender = msgs[i].getOriginatingAddress();
                str += " :";
                str += msgs[i].getMessageBody().toString();
                message = msgs[i].getMessageBody().toString();
                str += "\n";
            }
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            //Créer un objet SMS et envoie un auto message
            SMS sms = new SMS(message, sender);
            sms.autoResponseSMS();
        }
    }}

