package com.developer.pritampc.mysmsreciever;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.MyViewHolder> {
    private ArrayList<String> sender_number=new ArrayList<>();
    private ArrayList<String> sender_msg=new ArrayList<>();
    private Context context;

    public MyRecycleAdapter(ArrayList<String> sender_number, ArrayList<String> sender_msg, Context context) {
        this.sender_number = sender_number;
        this.sender_msg = sender_msg;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return sender_msg.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView num,msg;
        public MyViewHolder(View itemView) {
            super(itemView);
            num=itemView.findViewById(R.id.tv_num);
            msg=itemView.findViewById(R.id.tv_msg);

        }
    }
}
