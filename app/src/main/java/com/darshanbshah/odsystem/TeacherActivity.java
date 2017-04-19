package com.darshanbshah.odsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TeacherActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference root;
    DatabaseReference od;
    DatabaseReference adv;
    DatabaseReference student;

    TextView t;
    List<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        mAuth = FirebaseAuth.getInstance();
        root = database.getReference();
        od = root.child("OD");
        adv = root.child("Advisors");
        student = root.child("Student");
        t = (TextView)findViewById(R.id.textView);

        od.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    Toast.makeText(getApplicationContext(), dsp.getKey().toString(), Toast.LENGTH_SHORT).show();
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

        student.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    if (dsp.getKey().toString().equals("Email"))  {
                        t.setText(dsp.getValue().toString());
                    }
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

        for (String advName : list) {

        }
    }

    public void signOut(View view) {
        mAuth.signOut();
        startActivity(new Intent(this, Login.class));
        finish();
    }
}
