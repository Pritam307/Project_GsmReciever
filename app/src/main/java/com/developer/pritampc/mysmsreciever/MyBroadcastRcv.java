package com.developer.pritampc.mysmsreciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by pritamPC on 3/24/2018.
 */

public class MyBroadcastRcv extends BroadcastReceiver {

    String value="";
    @Override
    public void onReceive(Context context, Intent intent) {
        final String TAG="SMS";
        final Bundle bundle=intent.getExtras();
        SmsMessage[] messages;
        String sms="";
        String phone_number="";
        //ArrayList<String> con_list;
        //ContactList list=new InsertContactFragment(); //interface initialization
        //con_list=list.getContact();
        String contact;
        Intent broadcastintent=new Intent();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                messages=new SmsMessage[pdus!=null?pdus.length:0];
                for(int i=0;i<messages.length;i++)
                {
                    messages[i]=SmsMessage.createFromPdu((byte[])(pdus!=null?pdus[i]:null));
                    contact=messages[i].getDisplayOriginatingAddress();

                    if(ifContactExist(context,contact)) {
                            phone_number += messages[i].getDisplayOriginatingAddress();
                            sms += messages[i].getDisplayMessageBody();
                            broadcastintent.setAction("SMS_RECEIVED_ACTION");
                            broadcastintent.putExtra("message",sms);
                            broadcastintent.putExtra("sender",phone_number);
                            context.sendBroadcast(broadcastintent);
                        }
                }



            }

    }

    public boolean ifContactExist(Context context,String contact)
    {
        SharedPreferences mypref=context.getSharedPreferences("MyPref",0);
        Map<String,?> myset=mypref.getAll();
        for (Map.Entry<String,?> entry: myset.entrySet())
        {
            value=entry.getValue().toString();
            if(value.equals(contact))
            {
                return true;
            }
        }
        return false;
    }

}
