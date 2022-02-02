package com.example.sms_bomber;

import android.app.Activity;
import android.telephony.SmsManager;

public class SMS extends Activity {
    String number;
    String message;
    String sender;
    int count;

    SMS (String number, String message, int count) {
        this.number = number;
        this.message = message;
        this.count = count;
    }

    SMS (String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public void sendSMS() {
        for (int i = 0; i < this.count; i++){
            SmsManager Manager = SmsManager.getDefault();
            Manager.sendTextMessage(this.number, null, this.message, null, null);
        }
    }

    public void autoResponseSMS() {
        SMS tempSMS = new SMS(this.sender, "Je te dis ça tout à l'heure.", 1);
        tempSMS.sendSMS();
    }

}
