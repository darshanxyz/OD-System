package com.darshanbshah.odsystem;

import android.app.ProgressDialog;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RollNumber extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private EditText rollNumber;
    private Spinner advisors;

    List<String> list = new ArrayList<String>();
    List<String> lst = new ArrayList<String>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference root;
    DatabaseReference student;
    DatabaseReference adv;
    DatabaseReference id;
    DatabaseReference roll_no;
    DatabaseReference email;
    DatabaseReference advisor;

    HashMap <String,String> adv_map = new HashMap <String, String>();
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FirebaseUser user = mAuth.getCurrentUser();
        root = database.getReference();
        student = root.child("Student");
        adv = root.child("Advisors");
        id = student.child(user.getUid());
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
                    list.add(String.valueOf(dsp.getKey()));
                    Log.e("VALUE: ", String.valueOf(dsp.getKey()));
                }
                Log.e("LISTSIZE", String.valueOf(list.size()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.advisors, android.R.layout.simple_spinner_dropdown_item);
        advisors.setAdapter(adapter);
        advisors.setOnItemSelectedListener(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait");
        dialog.show();



        adv.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    lst.add(String.valueOf(dsp.getValue()));
                    Log.e("EMAILS: ", String.valueOf(dsp.getValue()));
                }
                if (lst.contains(mAuth.getCurrentUser().getEmail())) {
                    startActivity(new Intent(getApplicationContext(), TeacherActivity.class));
                    finish();
                }
                else {
                    dialog.hide();
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

    int flag = 0;
    public void nextAct(View view) {
        if (TextUtils.isEmpty(rollNumber.getText().toString())) {
            Toast.makeText(this, "Enter Roll Number", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.e("LST_SIZE", String.valueOf(lst.size()));
            for (String email : lst) {
                if (email.equals(mAuth.getCurrentUser().getEmail())) {
                    startActivity(new Intent(this, TeacherActivity.class));
                    flag = 1;
                    finish();
                    break;
                }
            }
            if (flag == 0) {
                roll_no.setValue(rollNumber.getText().toString());
                email.setValue(mAuth.getCurrentUser().getEmail());
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }
    }

    public void signOut(View view) {
        mAuth.signOut();
        startActivity(new Intent(this, Login.class));
        finish();
    }
}