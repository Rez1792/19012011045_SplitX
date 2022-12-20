package com.example.splitx;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PassBookFragment extends Fragment {
    LinearLayoutManager linearLayout;
    RecyclerView recyclerView;
    PassBookAdapter passBookAdapter;
    ArrayList<PassBookObject> passBookData = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PassBookFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_pass_book, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.passBookRecycler);
        recyclerView.setHasFixedSize(true);
        linearLayout = new LinearLayoutManager(getContext());
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayout);

        db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("PassBook").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            PassBookObject obj = documentSnapshot.toObject(PassBookObject.class);
                            passBookData.add(obj);
                        }
                        passBookAdapter = new PassBookAdapter(passBookData,getContext());
                        recyclerView.setAdapter(passBookAdapter);
                    }
                });

        return  v;
    }
}