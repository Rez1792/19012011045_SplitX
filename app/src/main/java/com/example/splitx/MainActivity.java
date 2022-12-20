package com.example.splitx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Fragment CardsFragment;
    private Fragment SettingsFragment;
    private Fragment PassBookFragment;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUser;
    private BottomNavigationView bottomNavigationItemView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        bottomNavigationItemView = findViewById(R.id.bottom_navigation);

    bottomNavigationItemView.setItemActiveIndicatorColor(getColorStateList(R.color.dialogBoxColor));
        bottomNavigationItemView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.join_room:
                        MaterialAlertDialogBuilder materialAlertDialogBuilder =  new MaterialAlertDialogBuilder(MainActivity.this,R.style.ThemeOverlay_App_MaterialAlertDialog);
                        View mView = getLayoutInflater().inflate(R.layout.room_name_dilaog,null);
                        final EditText roomIdEt = (EditText)mView.findViewById(R.id.roomNameEt);
                        materialAlertDialogBuilder.setTitle("Join Room");
                        materialAlertDialogBuilder.setView(mView);
                        roomIdEt.setHint("Room Id");
                        materialAlertDialogBuilder.setPositiveButton("Join", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                List<String> joinedRooms = new ArrayList<>();
                                joinedRooms.add(roomIdEt.getText().toString());
                                JoinedRoomObject obj2 = new JoinedRoomObject(joinedRooms);
                                db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("JoinedRooms").limit(1).get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                if(queryDocumentSnapshots.getDocuments().size()>0){
                                                    db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("JoinedRooms").document("rooms").update("joinedRooms", FieldValue.arrayUnion(roomIdEt.getText().toString()));
                                                }else{
                                                    db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("JoinedRooms").document("rooms").set(obj2);
                                                }
                                                List<String> joinedUsers = new ArrayList<>();
                                                joinedUsers.add(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                                DetailsObject detailsObject = new DetailsObject(joinedUsers);
                                                db.collection("Rooms").document(roomIdEt.getText().toString()).collection("Details").limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                        if(queryDocumentSnapshots.getDocuments().size()>0){
                                                            db.collection("Rooms").document(roomIdEt.getText().toString()).collection("Details").document("UserDetails").update("joinedUsers",FieldValue.arrayUnion(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                                                        }else{
                                                            db.collection("Rooms").document(roomIdEt.getText().toString()).collection("Details").document("UserDetails").set(detailsObject);
                                                        }
                                                    }
                                                });
                                              db.collection("Users").document(currentUser).get()
                                                      .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                          @Override
                                                          public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                              db.collection("Rooms").document(roomIdEt.getText().toString()).update("userPhoto",FieldValue.arrayUnion(documentSnapshot.get("profileUri").toString()));
                                                          }
                                                      });
                                                db.collection("Rooms").document(roomIdEt.getText().toString()).get()
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                               int tmp = Integer.parseInt(String.valueOf(documentSnapshot.get("numberOfpeople")));
                                                                db.collection("Rooms").document(roomIdEt.getText().toString()).update("numberOfpeople",String.valueOf(tmp+1));
                                                            }
                                                        });


                                                Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Room joined successfully", Snackbar.LENGTH_LONG);
                                                snackBar.setBackgroundTint(Color.parseColor("#b2fab4"));
                                                snackBar.setTextColor(Color.BLACK);
                                                snackBar.show();
                                            }
                                        });

                                CardsFragment = new CardsFragment();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragContainer,CardsFragment)
                                        .commit();
                            }
                        })
                                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                        materialAlertDialogBuilder.show();
                        return true;
                        case R.id.cards:
                        CardsFragment = new CardsFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer,CardsFragment).commit();
                        return true;
                    case R.id.account:
                        SettingsFragment = new SettingsFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer,SettingsFragment).commit();
                        return true;
                    case R.id.PassBook:
                        PassBookFragment = new PassBookFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer,PassBookFragment).commit();
                        return true;
                }
                return false;
            }
        });
        CardsFragment = new CardsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragContainer,CardsFragment)
                .commit();
    }

}