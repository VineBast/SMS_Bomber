package com.example.sms_bomber;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button sendButton;
    EditText phoneNumber;
    EditText messageText;
    EditText countMessage;
    Spinner listMessage;
    Button majButton;
    EditText majMessage;
    ArrayList<String> autoMessages;
    ArrayList<String> numbersList;
    Spinner phonesList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Permissions et contacts dans un Cursor :
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_CONTACTS}, PackageManager.PERMISSION_GRANTED);
        Cursor contacts = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        Intent i = new Intent(this, SmsService.class);
        i.putExtra("Key", "Value for the service");
        this.startService(i);

        Context context = this;
        numbersList = new ArrayList<String>();
        autoMessages = new ArrayList<String>();

        phonesList = (Spinner) findViewById(R.id.phones_list);
        listMessage = (Spinner) findViewById(R.id.list_message);
        majMessage = (EditText) findViewById(R.id.auto_message);
        majButton = (Button) findViewById(R.id.MAJ_button);
        sendButton = (Button) findViewById(R.id.send_button);
        phoneNumber = (EditText) findViewById(R.id.phone_number);
        messageText = (EditText) findViewById(R.id.message);
        countMessage = (EditText) findViewById(R.id.countMessage);


        //Met à jour le spinner des contacts
        while (contacts.moveToNext()) {
            @SuppressLint("Range") String number = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            numbersList.add(number);
        }
        ArrayAdapter<String> adapterNumber = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, numbersList);
        phonesList.setAdapter(adapterNumber);
        contacts.close();

        //Met à jour le spinner des messages auto après clic (et nettoie l'entrée), sinon envoie une alerte dans un toast
        majButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String autoMessage = majMessage.getText().toString();
                if (autoMessage.length() > 0) {
                    autoMessages.add(autoMessage);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, autoMessages);
                    listMessage.setAdapter(adapter);
                    majMessage.getText().clear();
                } else
                    Toast.makeText(getBaseContext(), "Entre un message automatique avant de MAJ.", Toast.LENGTH_SHORT).show();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String phone;
                String phoneFromEdit = phoneNumber.getText().toString();
                String phoneFromSpinner = phonesList.getSelectedItem().toString();
                String message = listMessage.getSelectedItem().toString();
                String count = countMessage.getText().toString();

                //Si un numéro a été entré manuellement, choisis celui-ci, sinon choisis le premier numéro des contacts
                if (phoneFromEdit.length() > 0)
                    phone = phoneFromEdit;
                else
                    phone = phoneFromSpinner;

                //Envoie un SMS si le formulaire est complété (et nettoie les entrée) sinon envoie une alerte dans un toast
                if (phone.length() > 0 && message.length() > 0 && count.length() > 0) {
                    SMS sms = new SMS(phone, message, Integer.valueOf(count));
                    sms.sendSMS();
                    countMessage.getText().clear();
                    phoneNumber.getText().clear();
                } else
                    Toast.makeText(getBaseContext(), "Entre un numéro de téléphone, un message et le nombre de messages à envoyer.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}