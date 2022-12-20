package com.example.splitx;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PassBookAdapter extends RecyclerView.Adapter<PassBookAdapter.ViewHolder>{
    private ArrayList<PassBookObject> passBookData = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    Context context;

    public PassBookAdapter(ArrayList<PassBookObject> passBookData, Context context) {
        this.passBookData = passBookData;
        this.context = context;
    }
    public PassBookAdapter(){

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_passbook, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.sentDateTime.setText(passBookData.get(position).getTime()+" | "+passBookData.get(position).getDate());

        if(passBookData.get(position).getStatus().equals("responseCode=0")){
            holder.status.setText("Success");
            holder.status.setTextColor(Color.GREEN);
        }else{
            holder.status.setTextColor(Color.RED);
            holder.status.setText("Failure");
        }
        if(passBookData.get(position).getFlag() == 1){
            holder.amount.setText("+ ₹"+passBookData.get(position).getAmount());
            holder.senderUpi.setText(passBookData.get(position).getUpi());
        }else{
            holder.amount.setText("- ₹"+passBookData.get(position).getAmount());
            holder.senderUpi.setText(passBookData.get(position).getUpi2());
        }
    }


    @Override
    public int getItemCount() {
        return passBookData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView sentDateTime, amount, status, senderUpi;

        public ViewHolder(View itemView) {
            super(itemView);
            sentDateTime = itemView.findViewById(R.id.sentDateTime);
            amount = itemView.findViewById(R.id.paidAmount);
            status = itemView.findViewById(R.id.status);
            senderUpi = itemView.findViewById(R.id.senderUpi);
        }
    }
}
