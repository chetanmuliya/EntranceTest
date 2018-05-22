package com.example.chetanmuliya.entrancetest.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.chetanmuliya.entrancetest.Interface.OnClickQuestionNos;
import com.example.chetanmuliya.entrancetest.activity.MainActivity;
import com.example.chetanmuliya.entrancetest.R;

/**
 * Created by Abhishek on 11/9/2017.
 */

public class CustomAdapterForQuestions extends RecyclerView.Adapter<CustomAdapterForQuestions.MyViewHolder> {

    Context c;
  int[] number;
    public static boolean clicked=false;
    private OnClickQuestionNos onClickQuestionNos;

    public CustomAdapterForQuestions(Context ctx,int[] no,OnClickQuestionNos onClickQuestionNos) {
        this.c=ctx;
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
        holder.questionNo.setText(number[position]);

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
