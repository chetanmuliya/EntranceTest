package com.example.chetanmuliya.entrancetest.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.chetanmuliya.entrancetest.Interface.OnClickQuestionNos;
import com.example.chetanmuliya.entrancetest.activity.MainActivity;
import com.example.chetanmuliya.entrancetest.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Abhishek on 11/9/2017.
 */

public class CustomAdapterForQuestions extends RecyclerView.Adapter<CustomAdapterForQuestions.MyViewHolder> {

    Context c;
  int[] number;
  private List<Boolean> answered;
  private List<String> status;
    public static boolean clicked=false;
    private OnClickQuestionNos onClickQuestionNos;

    public CustomAdapterForQuestions(Context ctx, List<Boolean> answered, List<String> status,int[] no, OnClickQuestionNos onClickQuestionNos) {
        this.c=ctx;
        this.answered=answered;
        for (int i =0 ;i< no.length ;i++){
            answered.add(i,false);
        }
        this.status = status;
        for (int i =0 ;i< no.length ;i++){
            status.add(i,"color");
        }
        this.number=no;
        this.onClickQuestionNos=onClickQuestionNos;
    }

    @Override
    public CustomAdapterForQuestions.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View layout=inflater.inflate(R.layout.custom_layout_forquestionbox,parent,false);
        return new MyViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(CustomAdapterForQuestions.MyViewHolder holder, final int position) {
          clicked=false;
        holder.questionNo.setText(""+number[position]);
        if(answered.get(position).equals(true)){
            holder.questionNo.setBackgroundColor(Color.GREEN);
        }
        if(status.size()>0) {
            if (status.get(position).equals("yellow")) {
                holder.questionNo.setBackgroundColor(Color.YELLOW);
            }else if (status.get(position).equals("green")){
                holder.questionNo.setBackgroundColor(Color.GREEN);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickQuestionNos.onClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return number.length;
    }

    public void setAnswered(int index,boolean b) {
        answered.add(index,b);
        notifyItemChanged(index);
    }
    public void setStatus(int index,String color) {
        status.add(index,color);
        notifyItemChanged(index);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView questionNo;
        public MyViewHolder(View itemView) {
            super(itemView);
            questionNo=(TextView)itemView.findViewById(R.id.number);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            onClickQuestionNos.onClick(view,getAdapterPosition());
        }
    }
}
