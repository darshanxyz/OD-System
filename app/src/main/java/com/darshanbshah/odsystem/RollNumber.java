package com.darshanbshah.odsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.MainThread;
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

    DatabaseReference root = database.getReference();
    DatabaseReference student = root.child("Student");
    DatabaseReference id = student.child(mAuth.getCurrentUser().getUid());
    DatabaseReference roll_no = id.child("RollNumber");
    DatabaseReference email = id.child("Email");
    DatabaseReference advisor = id.child("Advisor");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_number);
        rollNumber = (EditText)findViewById(R.id.rollNumberEditText);
        advisors = (Spinner)findViewById(R.id.spinner);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.advisors, android.R.layout.simple_spinner_dropdown_item);
        advisors.setAdapter(adapter);
        advisors.setOnItemSelectedListener(this);


        SharedPreferences preferences = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);

        if(preferences.getBoolean("activity_executed", false)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean("activity_executed", true);
            edit.commit();
        }


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = (TextView)view;
        advisor.setValue(textView.getText());
        Toast.makeText(this, textView.getText(), Toast.LENGTH_SHORT).show();
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
