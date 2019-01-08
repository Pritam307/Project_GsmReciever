package com.developer.pritampc.mysmsreciever;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.logicalAnd;


public class InsertContactFragment extends Fragment implements  ContactList,View.OnClickListener{

    private Context context;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Button add_profile;
    public ArrayList<String> name_list=new ArrayList<>();
   // public ArrayList<String> imgList=new ArrayList<>();
    public Set<String> con=new HashSet<>();
    private HashMap<String,String> person_list =new HashMap<>();
    private ListView list_people;
    private ArrayAdapter<String> adapter;
    //private Toolbar toolbar;
    private static final String TAG="SET";
    private static final int PICK_IMAGE_REQUEST=100;

    private ImageView edit_profile;
    private CircleImageView profile_img;
    private EditText ed_name,ed_age,ed_number,ed_;
    private Button save,clear;
    private RxPermissions permissions;
    private View view;
    Uri uri;

    public InsertContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_insert_contact, container, false);

        //toolbar=getActivity().findViewById(R.id.cus_toolbar);
        //toolbar.setVisibility(View.INVISIBLE);
        add_profile=view.findViewById(R.id.open_profile);
        pref=getActivity().getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        //imgpref=getActivity().getSharedPreferences("ImgPref",Context.MODE_PRIVATE);
        editor=pref.edit();
        //imgedit=imgpref.edit();
        //editor.clear();
       // name_list.clear();
        list_people=view.findViewById(R.id.contact_list);
        getContact();
        convertToList(con);
        adapter=new MyListAdapter(this.context,name_list,person_list);
        //adapter=new ArrayAdapter<String>(context,R.layout.insert_list_layout,name_list);
        list_people.setAdapter(adapter);
        add_profile.setOnClickListener(this);
        Log.d(TAG, "onCreateView: "+name_list);

        list_people.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.d(TAG, "onItemLongClick: "+name_list.get(i));

                //if(name_list.size()==0) editor.clear();
                //person_list.remove(name_list.get(i));

                //adapter.remove(name_list.get(i));
                //adapter.notifyDataSetChanged();
                //name_list.remove(i);
                //getContact();
                //convertToList(con);
                return true;
            }
        });


        return  view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;

    }

    public void getData(String name,String number)
    {
        if(name!=null && number!=null)
        {
            editor.putString(name,number);
           // imgedit.putString(name,imgpath);//save imgpath against name
            editor.commit();
           // imgedit.commit();

        }else
        {
            return;
        }
    }

    @Override
    public void getContact() {
        Map<String,?> list=pref.getAll();
        //Map<String,?> imgPathList=imgpref.getAll();
        for (Map.Entry<String,?> entry: list.entrySet())
        {
            con.add(entry.getKey().toString());
            person_list.put(entry.getKey().toString(),entry.getValue().toString());
            Log.d(TAG, "getContact Keys: "+person_list.keySet());
            //Log.d(TAG, "getContact Values: "+person_list.values());
        }


    }

    private void convertToList(Set<String> set)
    {
        for(String s: set)
        {
            name_list.add(s);
        }
 
        for (int i=0;i<name_list.size();i++)
        {
            for (int j=i+1;j<name_list.size();j++)
            {
                if(name_list.get(i).equals(name_list.get(j)))
                {
                    name_list.remove(j);
                    j--;
                }
            }
        }
        Log.d(TAG, "convertToList: "+name_list);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.open_profile:
                //Intent intent=new Intent(context,TouchPadActivity.class);
                //startActivity(intent);
                profile_dialog();
                break;
        }
    }


    public void profile_dialog()
    {
        Dialog dialog=new Dialog(getActivity());
        dialog.setCancelable(true);
        View view=getActivity().getLayoutInflater().inflate(R.layout.fragment_profile,null);
        dialog.setContentView(view);

        ed_name=view.findViewById(R.id.patient_name);
        ed_age=view.findViewById(R.id.patient_age);
        ed_number=view.findViewById(R.id.patient_number);
        save=view.findViewById(R.id.btn_save);
        clear=view.findViewById(R.id.btn_clear);
        edit_profile=view.findViewById(R.id.profile_edit);
        profile_img=view.findViewById(R.id.profile_image);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(ed_name.getText().toString().trim()) && !TextUtils.isEmpty(ed_number.getText().toString()))
                {
                    getData(ed_name.getText().toString().trim(),ed_number.getText().toString().trim());
                    getContact();
                    convertToList(con);
                   // Log.d(TAG, "onClick: "+uri.toString());
                    //Log.d(TAG, "onClick: "+con);
                    Log.d(TAG, "onClick: "+name_list);
                    list_people.setAdapter(adapter);
                }

                dialog.dismiss();
            }
        });


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                name_list.clear();
                con.clear();
                dialog.dismiss();
            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(intent,"Pick Picure"),PICK_IMAGE_REQUEST);

            }
        });

        dialog.show();
        Window window=dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode==PICK_IMAGE_REQUEST && data!=null)
        {
            //Log.d(TAG, "onActivityResult 1st if: Executed ");
            if(resultCode==RESULT_OK && data.getData()!=null)
            {

                if(Build.VERSION.SDK_INT<19)
                {
                    uri=data.getData();
                }else
                    {
                        uri=data.getData();
                        final int takeflags=data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        try{
                            getActivity().getContentResolver().takePersistableUriPermission(uri,takeflags);
                            //Log.d(TAG, "onActivityResult: ty catch block" +uri);
                            Bitmap bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
                            profile_img.setImageBitmap(bitmap);

                        }catch (Exception e)
                        {
                            Log.d(TAG, "onActivityResult: "+e.getMessage());
                        }
                    }
            }
        }
    }


}
