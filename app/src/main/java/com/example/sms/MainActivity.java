package com.example.sms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText etMessage;
    EditText etTelNr;
    int MY_PERMISSONS_SEND_SMS=100;
    String SENT="SMS_SENT";
    String DElIVERED="SMS_DELIVERED";
    PendingIntent sentPI,deliveredPI;
    BroadcastReceiver smsSentReceiver,smsDeliveredReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etMessage = findViewById(R.id.etMessage);
        etTelNr = findViewById(R.id.etTelNr);

        sentPI=PendingIntent.getBroadcast(this,0,new Intent(SENT),0);
        deliveredPI=PendingIntent.getBroadcast(this,0,new Intent(DElIVERED),0);

    }
    //es cragri lifeCiclic mi masa
    @Override
    protected void onResume() {
        super.onResume();
        smsSentReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this,"SMS Sent!",Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(MainActivity.this,"Generic failure!",Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(MainActivity.this,"No Service!",Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NULL_PDU:
                         Toast.makeText(MainActivity.this,"NULL PDU!",Toast.LENGTH_SHORT).show();
                         break;

                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(MainActivity.this,"Radio off!",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };


        smsDeliveredReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this,"SMS Delivered!",Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(MainActivity.this,"SMS not delivered!",Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };


        //aysinqn esi spasselu a minchev sent inten@ lini u kanchvuma hamapatasxna smsSent metod@ verevi
        registerReceiver(smsSentReceiver,new IntentFilter(SENT));
        //es e delivered@ lin
        registerReceiver(smsDeliveredReceiver,new IntentFilter(DElIVERED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(smsSentReceiver);
        unregisterReceiver(smsDeliveredReceiver);
    }

    public void btn_sendSMS_OnClick (View v)
        {
         String message=etMessage.getText().toString();
         String telNr=etTelNr.getText().toString();
           if(ContextCompat .checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
               ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},MY_PERMISSONS_SEND_SMS);
           }
           else{
               SmsManager sms=SmsManager.getDefault();
               sms.sendTextMessage(telNr,null,message,sentPI,deliveredPI);
           }
        }

}