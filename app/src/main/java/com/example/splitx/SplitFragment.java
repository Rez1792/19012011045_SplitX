package com.example.splitx;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplitFragment extends Fragment {
    RecyclerView seulaRecyclerView;
    private SplitAmountListener spaListener;
    LinearLayoutManager linearLayout;
    Context context;
    EditText splitAmoundEt;
    SEULA_Object seula_object;
    Fragment me = this;
    int cnt = 0;
    String roomId;
    String splitAmount;
    public String amountEt;
    TextView numberOfJoinedPeople;
    public List<String> paidList = new ArrayList<>();
    public List<String> UnPaidList = new ArrayList<>();
    private ArrayList<SEULA_Object> seulaData = new ArrayList<>();
    Map<String,String> otherDetailsMap = new HashMap<>();
    Map<String,List<String>> userData = new HashMap<>();
    public ArrayList<SEULA_Object_For_Fire> seulaDataForFire = new ArrayList<>();
    ArrayList<SEULA_Object> seula_objectsList = new ArrayList<>();
    SEUL_Adapter SEULAdapter;
    List<String> userEmails = new ArrayList<>();
    MaterialToolbar materialToolbar;
    EditText splitNote;
    Button sendRequestButton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public SplitFragment() {
    }

    public  SplitFragment(Context context,String roomId){
        this.context = context;
        this.roomId = roomId;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public interface SplitAmountListener {
        void onAmountChange(String text);
    }

    public void seulData(ArrayList<SEULA_Object> userList, Float splitSum){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_split, container, false);

//            spaListener = (SplitAmountListener) getChildFragmentManager();

        seulaRecyclerView = v.findViewById(R.id.joinedUserRecyler);
        seulaRecyclerView.setHasFixedSize(true);
        linearLayout = new LinearLayoutManager(getContext());
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        seulaRecyclerView.setLayoutManager(linearLayout);
        splitAmoundEt = v.findViewById(R.id.amount);
        materialToolbar = v.findViewById(R.id.topAppBarRoomActivity);
        numberOfJoinedPeople = v.findViewById(R.id.numberOfJoinedPeople);
        sendRequestButton = v.findViewById(R.id.sendRequestButton);
        splitNote = v.findViewById(R.id.splitNote);
        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RoomActivity)getContext()).changeFragment();
            }
        });
        splitAmoundEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                amountEt = splitAmoundEt.getText().toString();
                if(!amountEt.isEmpty()) {
                    SEULAdapter = new SEUL_Adapter(seula_objectsList, getContext(), amountEt,SplitFragment.this);
                    seulaRecyclerView.setAdapter(SEULAdapter);
                }
            }
        });

        db.collection("Rooms").document(((RoomActivity)getActivity()).roomId).collection("Details").document("UserDetails").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                       userEmails = (List<String>) documentSnapshot.get("joinedUsers");
                       for(int i=0;i<userEmails.size();i++){
                           db.collection("Users").document(userEmails.get(i)).get()
                                   .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                       @Override
                                       public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            seula_object = new SEULA_Object(documentSnapshot.get("name").toString(),documentSnapshot.get("profileUri").toString(),documentSnapshot.get("email").toString(),false);
                                            seula_objectsList.add(seula_object);
                                            SEULAdapter = new SEUL_Adapter(seula_objectsList,getContext(),"0",SplitFragment.this);
                                            seulaRecyclerView.setAdapter(SEULAdapter);
                                       }
                                   });
                       }
                    }
                });
        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cnt>0 && !splitAmoundEt.getText().toString().isEmpty() && !splitNote.getText().toString().isEmpty()) {
                    Calendar cal = Calendar.getInstance();
                    otherDetailsMap.put("Date", cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH));
                    otherDetailsMap.put("SplitSender", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    otherDetailsMap.put("Amount", splitAmount);
                    otherDetailsMap.put("SplitNote", splitNote.getText().toString());
                    userData.put("Paid", paidList);
                    userData.put("UnPaid", UnPaidList);
                    SEULA_Object_For_Fire obj = new SEULA_Object_For_Fire(otherDetailsMap, userData);
                    db.collection("Rooms").document(roomId).collection("SplitRequests").document(splitNote.getText().toString()).set(obj);
                    ((RoomActivity) getContext()).changeFragment();
                }else if(cnt<=0){
                    Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Atleast select one user", Snackbar.LENGTH_LONG);
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
                }else if(splitNote.getText().toString().isEmpty()){
                    Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Please write a note", Snackbar.LENGTH_LONG);
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
                }else if(splitAmoundEt.getText().toString().isEmpty()){
                    Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Amount should be atleast 1Rs", Snackbar.LENGTH_LONG);
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
            }
        });

        return v;
    }
    public void dataBus(ArrayList<SEULA_Object> seulaData,String s,String splitAmount,int cnt){
        this.splitAmount = "0";
        this.cnt = 0;
        this.cnt = cnt;
        this.splitAmount = splitAmount;
        paidList.clear();
        UnPaidList.clear();
        numberOfJoinedPeople.setText("Split between "+s+" people");
        this.seulaData.addAll(seulaData);
        int flag = 0;
        for(int i=0;i<seulaData.size();i++){
            if(seulaData.get(i).selected == true){
                if(seulaData.get(i).getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                    paidList.add(seulaData.get(i).getEmail());
                    flag = 1;
                }else{
                    UnPaidList.add(seulaData.get(i).getEmail());
                }
            }
        }
        if(flag == 1){
            otherDetailsMap.put("PaidNumber","1");
        }else{
            otherDetailsMap.put("PaidNumber","0");
        }
    }
}