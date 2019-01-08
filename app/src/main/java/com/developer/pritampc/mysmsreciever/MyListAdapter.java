package com.developer.pritampc.mysmsreciever;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MyListAdapter extends ArrayAdapter<String> implements View.OnClickListener {
    private ArrayList<String> nameList;

    private HashMap<String, String> perlist;
    public Context context;
    private ImageView imgview;
    String number;

    public MyListAdapter(@NonNull Context context, ArrayList<String> nameList, HashMap<String, String> perlist) {
        super(context, R.layout.insert_list_layout, nameList);
        this.nameList = nameList;
        this.context = context;
        this.perlist = perlist;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = nameList.get(position);
        TextView tvname;
        ImageView msgview;


        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.insert_list_layout, parent, false);
            tvname = convertView.findViewById(R.id.tvname);
            msgview = convertView.findViewById(R.id.ivmsg);


            tvname.setText(name);
            msgview.setFocusable(false);
            msgview.setTag(position);
            msgview.setOnClickListener(this);


        } else {
            return convertView;
        }

        return convertView;

    }

    @Override
    public void onClick(View view) {
        int pos;
        switch (view.getId()) {
            case R.id.ivmsg:
                pos = (Integer) view.getTag();
                number = perlist.get(nameList.get(pos));//gives the number of patient of position
                Intent intent = new Intent(context, TouchPadActivity.class);
                intent.putExtra("phone", perlist.get(nameList.get(pos)));
                context.startActivity(intent);
                break;


        }

    }

    void removeAt(int position)
    {
        nameList.remove(position);
        perlist.remove(nameList.get(position));
    }


}