package com.example.splitx;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SEUL_Adapter extends RecyclerView.Adapter<SEUL_Adapter.ViewHolder>{
    private ArrayList<SEULA_Object> seulaData = new ArrayList<>();
    private ArrayList<SEULA_Object> newSeulaData = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    Context context;
    List<Integer> pos = new ArrayList<>();
    static int selectedUserCount = 0;
    String amount;
    float splitSum;
    SEULA_Object obj;
    SplitFragment fragment;
    public static int cnt = 0;

    public SEUL_Adapter(ArrayList<SEULA_Object> seulaData, Context context, String amount, SplitFragment fragment) {
        this.seulaData = seulaData;
        this.context = context;
        this.amount = amount;
        this.fragment = fragment;
    }
    public SEUL_Adapter(){

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.split_user_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        splitSum = Float.valueOf(amount);
        holder.userName.setText(seulaData.get(position).getName());

        Picasso.get().load(seulaData.get(position).getUserPhoto()).into(holder.userPhoto);

        if(seulaData.get(position).selected){
            holder.amount.setText("₹ " + getSplitAmount());
            holder.stlCheck.setChecked(true);
        }else{
            holder.amount.setText("₹ 0");
            holder.stlCheck.setChecked(false);
        }
        holder.splitUserItemCons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.stlCheck.isChecked()){
                    cnt--;
                    fragment.dataBus(seulaData,String.valueOf(cnt),getSplitAmount(),selectedUserCount);
                    obj = new SEULA_Object(seulaData.get(position).name,seulaData.get(position).getUserPhoto(),seulaData.get(position).getEmail(),false);
                    seulaData.remove(position);
                    seulaData.add(obj);
                }else{
                    cnt++;
                    obj = new SEULA_Object(seulaData.get(position).name,seulaData.get(position).getUserPhoto(),seulaData.get(position).getEmail(),true);
                    seulaData.remove(position);
                    seulaData.add(obj);
                    fragment.dataBus(seulaData,String.valueOf(cnt),getSplitAmount(),selectedUserCount);
                }
                update();
            }

        });
    }
    @Override
    public int getItemCount() {
        return seulaData.size();
    }
    public String getSplitAmount(){

        selectedUserCount = 0;
        for(int i=0;i<seulaData.size();i++){
            if(seulaData.get(i).selected == true){
                selectedUserCount++;
            }
        }
        splitSum/=selectedUserCount;
        return String.valueOf(splitSum);
    }
    public void update(){
        splitSum = 0;
        selectedUserCount = 0;
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox stlCheck;
        ImageView userPhoto;
        TextView userName, amount;
        ConstraintLayout splitUserItemCons;
        public ViewHolder(View itemView) {
            super(itemView);
            stlCheck = itemView.findViewById(R.id.sltCheck);
            userPhoto = itemView.findViewById(R.id.userPhoto);
            userName = itemView.findViewById(R.id.userNameTV);
            amount = itemView.findViewById(R.id.splitAmount);
            splitUserItemCons = itemView.findViewById(R.id.splitUserItemCons);
        }
    }
}
