package com.developer.pritampc.mysmsreciever;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class ReadSmsFrag extends Fragment {

    private RecyclerView recyclerView;
    private MyRecycleAdapter adapter;
    private ArrayList<String> sender_list = new ArrayList<>();
    private ArrayList<String> mesg_list = new ArrayList<>();
    private Context context;

    public ReadSmsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_read_sms, container, false);
        recyclerView=view.findViewById(R.id.message_list);
        adapter=new MyRecycleAdapter(sender_list,mesg_list,getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        readSMS();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);

    }

    public void readSMS() {
        int sender_index, msg_index;
        Uri uri = Uri.parse("content://sms/inbox");
        ContentResolver resolver = context.getContentResolver();
        Cursor sms_Cursor = resolver.query(uri, null, null, null, null);
        if (sms_Cursor != null) {
            sender_index = sms_Cursor.getColumnIndex("address");
            msg_index = sms_Cursor.getColumnIndex("body");
            if (msg_index < 0 || !sms_Cursor.moveToFirst()) return;
            do {
                String sender = sms_Cursor.getString(sender_index);
                String message = sms_Cursor.getString(msg_index);
                sender_list.add(sender);
                mesg_list.add(message);
            } while (sms_Cursor.moveToNext());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
