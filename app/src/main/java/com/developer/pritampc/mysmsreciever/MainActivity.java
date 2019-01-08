package com.developer.pritampc.mysmsreciever;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;

import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private TextView show_msg;
    private static final String TAG="reciever";
    private IntentFilter intent_filter;
    private ArrayList<String> con_list=new ArrayList<>();
    private RxPermissions permissions;
    //private FloatingActionButton addPeople;
    private ViewPager pager;
    private MyPagerAdapter adapter;


    private BroadcastReceiver reciever_intent = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Snackbar snackbar=Snackbar.make(findViewById(R.id.cordinator_lay),"Message Recieved from:  "+intent.getStringExtra("sender"),Snackbar.LENGTH_LONG);
            View tbiew=snackbar.getView();
            TextView tv=tbiew.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.YELLOW);
            tv.setTextSize(16);
            snackbar.show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        intent_filter=new IntentFilter();
        intent_filter.addAction("SMS_RECEIVED_ACTION");
        permissions=new RxPermissions(this);

        permissions.request(Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_SMS,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted->{
                    if(granted)
                    {
                        Toast.makeText(this,"Granted!",Toast.LENGTH_SHORT).show();
                    }else
                    {
                        Toast toast=Toast.makeText(this,"Failed to grant permission!",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();

                    }
                });
        FragmentManager fm=getSupportFragmentManager();
        //pager=findViewById(R.id.view_pager);
        //adapter=new MyPagerAdapter(fm,getApplicationContext());
        //pager.setAdapter(adapter);
        Fragment frag=new InsertContactFragment();
        FragmentTransaction transaction=fm.beginTransaction();
        transaction.replace(R.id.frag_holder,frag);
        transaction.addToBackStack(null);
        transaction.commit();



    }


    @Override
    protected void onResume() {
        //registerReceiver(reciever_intent,new IntentFilter("SMS_RECEIVED_ACTION"));
        registerReceiver(reciever_intent,intent_filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(reciever_intent);
        super.onPause();
    }



}
