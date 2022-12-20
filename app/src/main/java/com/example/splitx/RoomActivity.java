package com.example.splitx;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class RoomActivity extends AppCompatActivity  {
    public String roomName, roomId;
    Button splitExepenseBtn;
    MaterialToolbar topBar;
    String receiverEmail = null, amount = null, splitNote = null, rUpi = null;
    private Fragment SplitFrag;
    FrameLayout frameLayout;
    RecyclerView recyclerView;
    Fragment settlementFrag;
    SplitRequestAdapter splitRequestAdapter;
    LinearLayoutManager linearLayout;
    SEULA_Object_For_Fire seula_object_for_fire;
    String upi;

    private ArrayList<SEULA_Object_For_Fire> requestData = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        roomName = getIntent().getStringExtra("roomName");
        roomId = getIntent().getStringExtra("roomId");

        frameLayout  =findViewById(R.id.fragContainer);
        splitExepenseBtn = findViewById(R.id.splitExpenseBtn);
        topBar = findViewById(R.id.topAppBarRoomActivity2);
        topBar.setTitle(roomName);
        topBar.setSubtitle(roomId);

        recyclerView = (RecyclerView) findViewById(R.id.expenseCardRecyler);
        recyclerView.setHasFixedSize(true);
        linearLayout = new LinearLayoutManager(getApplicationContext());
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayout);

        db.collection("Rooms").document(roomId).collection("SplitRequests").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                             seula_object_for_fire = documentSnapshot.toObject(SEULA_Object_For_Fire.class);
                             requestData.add(seula_object_for_fire);
                        }
                        splitRequestAdapter = new SplitRequestAdapter(requestData,RoomActivity.this);
                        recyclerView.setAdapter(splitRequestAdapter);
                    }
                });
        topBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
     topBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
             switch (item.getItemId()) {
                 case R.id.more:
                     Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_SHORT).show();
                     return true;
             }
             return false;

         }
     });
//        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getApplicationContext())
////                .setTitle("")
        topBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        topBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.more:
                        return true;
                }
                return false;
            }
        });
        splitExepenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SplitFrag = new SplitFragment(getApplicationContext(),roomId);
                frameLayout.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer,SplitFrag).commit();
            }
        });

    }

    public void launchUPI(String pa, String pn, String tn, String am) throws AppNotFoundException {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", pa)
                .appendQueryParameter("pn", pn)
                .appendQueryParameter("tn", tn)
                .appendQueryParameter("am", am)
                .appendQueryParameter("cu", "INR")
                .build();
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivityForResult(intent, 1421);
        }catch (Exception e){
            Snackbar snackBar = Snackbar.make(this.findViewById(android.R.id.content), "No UPI app found", Snackbar.LENGTH_LONG);
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
//
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1421) {
            if (resultCode==RESULT_OK){
                        db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                              upi = documentSnapshot.get("upi").toString();
                                assert data != null;
                                String responseArr[] = data.getStringExtra("response").split("&");
//                if(responseArr[1].equals("responseCode=0")){
                                db.collection("Rooms").document(roomId).collection("SplitRequests").document(splitNote).get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                SEULA_Object_For_Fire o = documentSnapshot.toObject(SEULA_Object_For_Fire.class);
                                                Map<String,String> otherDetailsMap = new HashMap<>();
                                                Map<String, List<String>> userData = new HashMap<>();
                                                Map<String, List<String>> userData2 = new HashMap<>();
                                                 List<String> paidList = new ArrayList<>();
                                                 List<String> UnPaidList = new ArrayList<>();
                                                otherDetailsMap.putAll(o.getOtherDetailsMap());
                                                userData.putAll(o.getUserData());
                                                 paidList.addAll(userData.get("Paid"));
                                                 UnPaidList.addAll(userData.get("UnPaid"));
                                                userData.put("Paid",paidList);
                                                userData.put("UnPaid",UnPaidList);
                                                int cnt = Integer.parseInt(otherDetailsMap.get("PaidNumber"));
                                                cnt+=1;
                                                otherDetailsMap.replace("PaidNumber",String.valueOf(cnt));
                                                paidList.add(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                                for(int i=0;i<UnPaidList.size();i++){
                                                    if(UnPaidList.get(i).equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                                                        UnPaidList.remove(i);
                                                    }
                                                }

                                                db.collection("Rooms").document(roomId).collection("SplitRequests").document(splitNote).update("Paid", FieldValue.arrayUnion(userData2)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(getApplicationContext(), "S", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), "F", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                ;
                                                db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).update("otherDetailsMap",otherDetailsMap);
                                                Calendar cal = Calendar.getInstance();
                                                PassBookObject recievierObj = new PassBookObject(cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH),cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE),FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),upi,rUpi,amount,responseArr[1],1);
                                                PassBookObject senderobj = new PassBookObject(cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH),cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE),FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),upi,rUpi,amount,responseArr[1],0);
                                                db.collection("Users").document(receiverEmail).collection("PassBook").add(recievierObj);
                                                db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("PassBook").add(senderobj);
                                            }
                                        });
                            }
                        }) ;


//                }else{
//
//                }

            }
        }
    }
    public void changeFragment(){
        frameLayout.setVisibility(View.GONE);
    }
}