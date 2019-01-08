package com.developer.pritampc.mysmsreciever;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class TouchPadActivity extends AppCompatActivity implements View.OnClickListener {
     int SEND_SMS_REQUEST = 10;
    Button btn_1,btn_2,btn_3,btn_4,saveToeeprom;
    EditText input_msg;
    public ArrayList<String> msg_new_list=new ArrayList<>();
    String mText;
    String send="SMS_SEND";
    String delivered="SMS_DELIVERED";
    PendingIntent sentPI,deliverPI;
    BroadcastReceiver sentRcv,deliverRcv;
    private String mNumber;
    AlertDialog.Builder builder;
    HashMap<Integer,String> map=new HashMap<>();
    public int index=0;
    private static final String TAG="SET";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customize_touch_pad);
        mNumber =getIntent().getStringExtra("phone");//get patient's number form Insert Fragment
        Toast.makeText(this,"Contact Number is: "+mNumber,Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onCreate: "+mNumber);
        btn_1=findViewById(R.id.but_one);
        btn_2=findViewById(R.id.but_two);
        btn_3=findViewById(R.id.but_three);
        btn_4=findViewById(R.id.but_four);
        //saveToeeprom=findViewById(R.id.save_button);
        //saveToeeprom.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);



    }



    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.but_one:
                index=1;
                getInputMsg();
                break;
            case R.id.but_two:
                index=2;
                getInputMsg();
                break;
            case R.id.but_three:
                index=3;
                getInputMsg();
                break;
            case R.id.but_four:
                index=4;
                getInputMsg();
                break;

        }
    }

    public void initiateSms(String phoneNumber)
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},SEND_SMS_REQUEST);
        }else
        {
            sendSms(phoneNumber,index);
        }
    }

    private void sendSms(String phnum,int index)
    {
        sentPI=PendingIntent.getBroadcast(this,0,new Intent(send),0);
        deliverPI=PendingIntent.getBroadcast(this,0,new Intent(delivered),0);

        sentRcv=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "Sms Sent", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic Failure!", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "NO SERVICE!", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU!", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio Off!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        deliverRcv=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "Sms Delivered!", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "Sms Not Delivered!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        registerReceiver(sentRcv,new IntentFilter(send));
        registerReceiver(deliverRcv,new IntentFilter(delivered));

        SmsManager sms=SmsManager.getDefault();
        sms.sendTextMessage(phnum,null,map.get(index),sentPI,deliverPI);

    }



    public void getInputMsg()
    {
        builder=new AlertDialog.Builder(TouchPadActivity.this);
        LayoutInflater inflater=getLayoutInflater();
        View dialogView=inflater.inflate(R.layout.dialoglayout,null);
        input_msg=dialogView.findViewById(R.id.alert_ed);
        builder.setView(dialogView);
        builder.setCancelable(true)
                .setPositiveButton(R.string.pos_but, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(R.string.nev_but, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();
        Button button=dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        button.setOnClickListener(new CustomListener(dialog));
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        sentRcv=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(getApplicationContext(),"Send Successfully!",Toast.LENGTH_LONG).show();
            }
        };

        deliverRcv=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(getApplicationContext(),"Delivered Successfully!",Toast.LENGTH_LONG).show();
            }
        };

        registerReceiver(sentRcv,new IntentFilter(send));
        registerReceiver(deliverRcv,new IntentFilter(delivered));
    }*/

    class CustomListener implements View.OnClickListener{
        private  Dialog dialog;
        private static final String TAG="SET";
       // private int indx;
        public CustomListener(Dialog dialog) {
            this.dialog = dialog;
            //this.indx=indx;
        }

        @Override
        public void onClick(View view) {
            mText=input_msg.getText().toString();
            if(mText.contains("#")||mText.contains("*"))
            {
                input_msg.setError(" Cannot enter # or * ");
                input_msg.requestFocus();
                return;
            }else if(mText.isEmpty())
            {
                input_msg.setError("Message is Empty!");
                input_msg.requestFocus();
                return;
            }else if(mText.length()>=15){
                input_msg.setError("Maximum limit is 15!");
                input_msg.requestFocus();
                return;

            }else {
                String str="#"+mText+"*";
                map.put(index,str);
                //msg_new_list.add(map.get(index));
                Log.d(TAG, "onClick: "+map.get(index));
                initiateSms(mNumber);
                dialog.dismiss();
            }
        }
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }
}
