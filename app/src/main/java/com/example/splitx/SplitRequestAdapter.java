package com.example.splitx;


import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SplitRequestAdapter extends RecyclerView.Adapter<SplitRequestAdapter.ViewHolder>{
    private ArrayList<SEULA_Object_For_Fire> requestData = new ArrayList<>();
    Context context;
    int paid, totalSize;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String,String> otherdeTails = new HashMap<>();
    String rUpi, rName;
     int flag = -1;

    public SplitRequestAdapter(ArrayList<SEULA_Object_For_Fire> requestData, Context context){
        this.requestData = requestData;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.request_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RoomActivity)context).receiverEmail = requestData.get(position).getOtherDetailsMap().get("SplitSender");
                ((RoomActivity)context).amount = requestData.get(position).getOtherDetailsMap().get("Amount");
                ((RoomActivity)context).splitNote = requestData.get(position).getOtherDetailsMap().get("SplitNote");
                db.collection("Users").document(requestData.get(position).otherDetailsMap.get("SplitSender")).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                rUpi = documentSnapshot.get("upi").toString();
                                rName = documentSnapshot.get("name").toString();
                                try {
                                    ((RoomActivity) context).launchUPI(rUpi, rName, requestData.get(position).otherDetailsMap.get("SplitNote"), requestData.get(position).otherDetailsMap.get("Amount"));
                                    ((RoomActivity)context).rUpi = rUpi;
                                }catch (Exception e){

                                }
                            }
                        });
            }
        });
        for(int i=0;i<requestData.get(position).userData.get("Paid").size();i++){
            if(requestData.get(position).userData.get("Paid").get(i).equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                flag = 0;
            }
        }
        for(int i=0;i<requestData.get(position).userData.get("UnPaid").size();i++){
            if(requestData.get(position).userData.get("UnPaid").get(i).equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                flag = 1;
            }
        }
        if(flag == 0){
            holder.paidUnpaid.setText("Paid");
            holder.pay.setText("Paid");
            holder.pay.setEnabled(false);
            holder.paidUnpaid.setVisibility(View.INVISIBLE);
            Toast.makeText(context, "Paid", Toast.LENGTH_SHORT).show();
        }else if(flag == 1){
            holder.paidUnpaid.setText("Unpaid | "+requestData.get(position).otherDetailsMap.get("Date"));
            holder.pay.setEnabled(true);
            Toast.makeText(context, "UnPaid", Toast.LENGTH_SHORT).show();
        }else{
            holder.pay.setText("No need to pay");
            holder.pay.setEnabled(false);
            Toast.makeText(context, "No need", Toast.LENGTH_SHORT).show();
            holder.paidUnpaid.setVisibility(View.INVISIBLE);
        }
         totalSize = requestData.get(position).userData.get("Paid").size()+requestData.get(position).userData.get("UnPaid").size();
         paid = requestData.get(position).userData.get("Paid").size();
        holder.outOfPaid.setText(paid+"/"+totalSize+" paid");
        if(!requestData.get(position).otherDetailsMap.get("SplitNote").equals(null)){
            holder.splitNote.setText(holder.splitNote.getText()+"for "+requestData.get(position).otherDetailsMap.get("SplitNote"));
        }else{
            holder.splitNote.setText("Split Request");
        }
        holder.amountReq.setText(holder.amountReq.getText()+""+requestData.get(position).otherDetailsMap.get("Amount"));
        holder.linearProgressIndicator.setProgress((int) Math.ceil(paid*(100/totalSize)));
    }
    @Override
    public int getItemCount() {
        return requestData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView amountReq, outOfPaid,paidUnpaid, splitNote;
        Button pay;
        LinearProgressIndicator linearProgressIndicator;
        public ViewHolder(View itemView) {
            super(itemView);
            pay = itemView.findViewById(R.id.button);
            linearProgressIndicator = itemView.findViewById(R.id.linearProgressIndicator);
            amountReq = itemView.findViewById(R.id.amountReq);
            outOfPaid = itemView.findViewById(R.id.textView4);
            paidUnpaid = itemView.findViewById(R.id.textView5);
            splitNote = itemView.findViewById(R.id.textView3);
        }
    }
}

