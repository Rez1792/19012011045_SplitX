package com.example.splitx;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder>{
    private ArrayList<RoomObject> cardData = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> userPhotoList = new ArrayList<>();
    Context context;
    int listSize = 1;

    public CardsAdapter(ArrayList<RoomObject> cardData, Context context) {
     this.cardData = cardData;
     this.context = context;
    }
    public CardsAdapter(){

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.card_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        listSize = cardData.get(position).getUserPhoto().size();
        holder.roomName.setText(cardData.get(position).getRoomName());
        holder.roomid.setText("Id: "+cardData.get(position).getRoomId());
        holder.peoples.setText(String.valueOf(cardData.get(position).getNumberOfpeople()));
        holder.cards.setText(cardData.get(position).getNumberOfCards());
        holder.numberOfPeopleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        if(listSize == 1){
            Picasso.get().load(cardData.get(position).getUserPhoto().get(0)).into(holder.u1);
        }
        else if(listSize == 2){
            holder.user2.setVisibility(View.VISIBLE);
            Picasso.get().load(cardData.get(position).getUserPhoto().get(0)).into(holder.u1);
            Picasso.get().load(cardData.get(position).getUserPhoto().get(1)).into(holder.u2);
        }else if(listSize == 3){
            holder.user2.setVisibility(View.VISIBLE);
            holder.user3.setVisibility(View.VISIBLE);
            Picasso.get().load(cardData.get(position).getUserPhoto().get(0)).into(holder.u1);
            Picasso.get().load(cardData.get(position).getUserPhoto().get(1)).into(holder.u2);
            Picasso.get().load(cardData.get(position).getUserPhoto().get(2)).into(holder.u3);
        }else if(listSize == 4){
            holder.user2.setVisibility(View.VISIBLE);
            holder.user3.setVisibility(View.VISIBLE);
            holder.user4.setVisibility(View.VISIBLE);
            Picasso.get().load(cardData.get(position).getUserPhoto().get(0)).into(holder.u1);
            Picasso.get().load(cardData.get(position).getUserPhoto().get(1)).into(holder.u2);
            Picasso.get().load(cardData.get(position).getUserPhoto().get(2)).into(holder.u3);
            Picasso.get().load(cardData.get(position).getUserPhoto().get(3)).into(holder.u4);
        }else{
            holder.numberOfPeopleTV.setText(String.valueOf(cardData.get(position).getUserPhoto().size()-4)+""+"+");
        }
        holder.cardItemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,RoomActivity.class);
                intent.putExtra("roomName",cardData.get(position).getRoomName());
                intent.putExtra("roomId",cardData.get(position).getRoomId());
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return cardData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView u1,u2,u3,u4;
        TextView roomid, roomName, numberOfPeopleTV;
        CardView user2,user3,user4;
        Button cards, peoples;
        CardView cardItemCard;
        public ViewHolder(View itemView) {
            super(itemView);
            u1 = itemView.findViewById(R.id.user1IV);
            u2 = itemView.findViewById(R.id.user2IV);
            u3 = itemView.findViewById(R.id.user3IV);
            u4 = itemView.findViewById(R.id.user4IV);
            roomid = itemView.findViewById(R.id.roomId);
            roomName = itemView.findViewById(R.id.roomName);
            cards = itemView.findViewById(R.id.numberOfCards);
            peoples = itemView.findViewById(R.id.numberOfPeopleBtn);
            numberOfPeopleTV = itemView.findViewById(R.id.user5IV);
            user2 = itemView.findViewById(R.id.user2);
            user3 = itemView.findViewById(R.id.user3);
            user4 = itemView.findViewById(R.id.user4);
            cardItemCard = itemView.findViewById(R.id.cardItemCard);
        }
    }
}