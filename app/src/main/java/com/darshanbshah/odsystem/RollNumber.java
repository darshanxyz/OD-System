package com.darshanbshah.odsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RollNumber extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private EditText rollNumber;
    private Spinner advisors;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference root;
    DatabaseReference student;
    DatabaseReference adv;
    DatabaseReference id;
    DatabaseReference roll_no;
    DatabaseReference email;
    DatabaseReference advisor;

    List<String> list = new ArrayList<String>();
    List<String> lst = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        root = database.getReference();
        student = root.child("Student");
        adv = root.child("Advisors");
        id = student.child(mAuth.getCurrentUser().getUid());
        roll_no = id.child("RollNumber");
        email = id.child("Email");
        advisor = id.child("Advisor");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_number);
        rollNumber = (EditText)findViewById(R.id.rollNumberEditText);
        advisors = (Spinner)findViewById(R.id.spinner);




        adv.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    list.add(String.valueOf(dsp.getKey())); //add result into array list
                    Log.e("VALUE: ", String.valueOf(dsp.getKey()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.advisors, android.R.layout.simple_spinner_dropdown_item);
        advisors.setAdapter(adapter);
        advisors.setOnItemSelectedListener(this);


        adv.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    lst.add(String.valueOf(dsp.getValue()));
                    Log.e("EMAILS: ", String.valueOf(dsp.getValue()));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




//        SharedPreferences preferences = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
//
//        if(preferences.getBoolean("activity_executed", false)){
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//        else {
//            SharedPreferences.Editor edit = preferences.edit();
//            edit.putBoolean("activity_executed", true);
//            edit.commit();
//        }


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = (TextView)view;
        for (String advisorName: list) {
            Log.d("EMAIL", advisorName);
            Log.d("TEXTVIEW", textView.getText().toString());
            if (textView.getText().equals(advisorName)) {
                int i = list.indexOf(advisorName);
                advisor.setValue(lst.get(i));
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void nextAct(View view) {
        if (TextUtils.isEmpty(rollNumber.getText().toString())) {
            Toast.makeText(this, "Enter Roll Number", Toast.LENGTH_SHORT).show();
        }
        else {
            roll_no.setValue(rollNumber.getText().toString());
            email.setValue(mAuth.getCurrentUser().getEmail());
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    public void signOut(View view) {
        mAuth.signOut();
        startActivity(new Intent(this, Login.class));
        finish();
    }
}
