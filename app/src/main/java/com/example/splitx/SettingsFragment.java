package com.example.splitx;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsFragment extends PreferenceFragmentCompat {
    EditTextPreference editTextPreference;
            Preference  name,email;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout v = (LinearLayout) super.onCreateView(inflater, container, savedInstanceState);
        Button btn = new Button(getActivity().getApplicationContext());
        Button save = new Button(getActivity().getApplicationContext());
        ImageView upi = new androidx.appcompat.widget.AppCompatImageView(getActivity().getApplicationContext()) {

        };
        editTextPreference = (EditTextPreference)  getPreferenceManager().findPreference("upi");
        name =  (Preference)  getPreferenceManager().findPreference("userName");
        email = (Preference)   getPreferenceManager().findPreference("UserEmail");
        db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                       editTextPreference.setTitle(documentSnapshot.get("upi").toString());
                       email.setTitle(documentSnapshot.get("email").toString());
                       name.setTitle(documentSnapshot.get("name").toString());

                    }
                });

        btn.setText("Log out");
        btn.setTextColor(Color.RED);
        upi.setImageResource(R.drawable.upicolor);

        v.addView(upi);
        v.addView(btn);
        v.addView(save);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(),GoogleAuthenticationActivity.class));
                getActivity().finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).update("upi",editTextPreference.getText())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Failed to update UPI id", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        save.setText("SAVE");
        save.setTextColor(Color.BLACK);
        return  v;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

    }
}