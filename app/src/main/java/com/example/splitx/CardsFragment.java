package com.example.splitx;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CardsFragment extends Fragment {
    private ExtendedFloatingActionButton createRoom;
    private EditText roomNameEt;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference roomRef = null;
    CardsAdapter cardsAdapter;
    List<String> myJoinedRooms = new ArrayList<>();
    ArrayList<RoomObject> roomObjects = new ArrayList<>();
    LinearLayoutManager linearLayout;
    RecyclerView recyclerView;
    RoomObject roomObject2 = null;
    public CardsFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_cards, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.cardRecycler);
        recyclerView.setHasFixedSize(true);
        linearLayout = new LinearLayoutManager(getContext());
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayout);

        db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("JoinedRooms").document("rooms").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        myJoinedRooms = (List<String>) documentSnapshot.get("joinedRooms");

                        db.collection("Rooms").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots){
                                    try {
                                        if (myJoinedRooms.size() > 0) {
                                            for (int i = 0; i < myJoinedRooms.size(); i++) {
                                                if (myJoinedRooms.get(i).equals(documentSnapshots.getId())) {
                                                    RoomObject roomObject = documentSnapshots.toObject(RoomObject.class);
                                                    roomObjects.add(roomObject);
                                                }
                                            }
                                        }
                                    }catch (Exception e){
                                    }
                                }
                                cardsAdapter = new CardsAdapter(roomObjects,getContext());
                                recyclerView.setAdapter(cardsAdapter);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
//
//        db.collection("Rooms").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
//                for(QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots){
//                    if(myJoinedRooms.size()>0) {
//                        for (int i = 0; i < myJoinedRooms.size(); i++) {
//                            if (myJoinedRooms.get(i).equals(documentSnapshots.getId())) {
//                                RoomObject roomObject = documentSnapshots.toObject(RoomObject.class);
//                                roomObjects.add(roomObject);
//                            }
//                        }
//                    }
//                }
//                cardsAdapter = new CardsAdapter(roomObjects,getContext());
//                recyclerView.setAdapter(cardsAdapter);
//            }
//        });

        createRoom = v.findViewById(R.id.createRoom);

        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                roomNameEt = v2.findViewById(R.id.roomNameEt);
                roomNameEt = new EditText(getContext());
                roomNameEt.setBackgroundResource(R.drawable.room_et_drawable);
                roomNameEt.setPadding(10,10,10,10);
                roomNameEt.setHint("Room Name");
                roomNameEt.setTextColor(Color.parseColor("#b2fab4"));
                roomNameEt.setHintTextColor(Color.parseColor("#b2fab4"));
                roomNameEt.setLayoutParams(new ViewGroup.LayoutParams(100, 200));


            MaterialAlertDialogBuilder obj =  new MaterialAlertDialogBuilder(getContext(),R.style.ThemeOverlay_App_MaterialAlertDialog);
                View mView = getLayoutInflater().inflate(R.layout.room_name_dilaog,null);
                final EditText txt_inputText = (EditText)mView.findViewById(R.id.roomNameEt);

                obj.setTitle("New Room");
                        obj.setView(mView);
                        obj.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String roomId = String.valueOf((int)(Math.random()*(100000-10000+1)+10000));
                                db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                String currentUserPhoto = documentSnapshot.get("profileUri").toString();
                                                List<String> currentUser = new ArrayList<>();
                                                currentUser.add(currentUserPhoto);
                                                roomObject2 = new RoomObject(roomId,txt_inputText.getText().toString(),"1","0",currentUser);
                                                db.collection("Rooms").document(roomId).set(roomObject2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        List<String> joinedUsers = new ArrayList<>();
                                                        joinedUsers.add(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                                        DetailsObject detailsObject = new DetailsObject(joinedUsers);
                                                        db.collection("Rooms").document(roomId).collection("Details").limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                if(queryDocumentSnapshots.getDocuments().size()>0){
                                                                    db.collection("Rooms").document(roomId).collection("Details").document("UserDetails").update("joinedUsers",FieldValue.arrayUnion(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                                                                }else{
                                                                    db.collection("Rooms").document(roomId).collection("Details").document("UserDetails").set(detailsObject);
                                                                }
                                                            }
                                                        });
                                            }
                                        });
                                       List<String> joinedRooms = new ArrayList<>();
                                       joinedRooms.add(roomId);
                                       JoinedRoomObject obj2 = new JoinedRoomObject(joinedRooms);
                                        db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("JoinedRooms").limit(1).get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                        if(queryDocumentSnapshots.getDocuments().size()>0){
                                                            db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("JoinedRooms").document("rooms").update("joinedRooms",FieldValue.arrayUnion(roomId));
                                                        }else{
                                                            db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("JoinedRooms").document("rooms").set(obj2);
                                                        }
                                                    }
                                                });
                                        Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Room created successfully", Snackbar.LENGTH_LONG);
                                        snackBar.setBackgroundTint(Color.parseColor("#b2fab4"));
                                        snackBar.setTextColor(Color.BLACK);
                                                snackBar.setAction("Close", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        snackBar.dismiss();
                                                    }
                                                });
                                                snackBar.setActionTextColor(Color.BLACK);
                                        snackBar.show();
                                            CardsFragment cardsFragment = new CardsFragment();
                                            getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.fragContainer,cardsFragment)
                                                .commit();
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Failed  to create room", Snackbar.LENGTH_LONG);
                                                snackBar.setBackgroundTint(Color.parseColor("#FF0000"));
                                                snackBar.setTextColor(Color.WHITE);
                                                snackBar.setAction("Close", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        snackBar.dismiss();
                                                    }
                                                });
                                                snackBar.setActionTextColor(Color.WHITE);
                                                snackBar.show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        obj.show();
            }
        });
        return v;
    }
}