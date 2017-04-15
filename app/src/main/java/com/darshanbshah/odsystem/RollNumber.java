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
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class RollNumber extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private EditText rollNumber;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference root = database.getReference();
    DatabaseReference user = root.child(mAuth.getCurrentUser().getUid());
    DatabaseReference roll_no = user.child("RollNumber");
    DatabaseReference email = user.child("Email");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_number);
        rollNumber = (EditText)findViewById(R.id.rollNumberEditText);
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if(pref.getBoolean("activity_executed", false)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean("activity_executed", true);
            ed.commit();
        }
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
