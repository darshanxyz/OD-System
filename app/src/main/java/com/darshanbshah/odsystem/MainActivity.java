package com.darshanbshah.odsystem;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView welcomeText;
    Calendar calendar = Calendar.getInstance();
    TextView from, to;
    FrameLayout frameLayout;
    EditText reason;
    String reasonString, fromDate, toDate, full;
    Boolean fullDay = false;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference root;
    DatabaseReference student;
    DatabaseReference adv;
    DatabaseReference id;
    DatabaseReference roll_no;
    DatabaseReference email;
    DatabaseReference advisor;

    String recepient = "";

    List<String> list1 = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        root = database.getReference();
        student = root.child("Student");
        adv = root.child("Advisors");
        id = student.child(mAuth.getCurrentUser().getUid());
        roll_no = id.child("RollNumber");
        email = id.child("Email");
        advisor = id.child("Advisor");


        welcomeText = (TextView)findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome " + mAuth.getCurrentUser().getEmail());
        from = (TextView)findViewById(R.id.fromDateTV);
        to = (TextView)findViewById(R.id.toDateTV);
        frameLayout = (FrameLayout) findViewById(R.id.hours_frame_layout);
        reason = (EditText)findViewById(R.id.reasonEditText);

        student.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    if (dsp.getKey().toString().equals("Advisor"))  {
                        list1.add(String.valueOf(dsp.getValue()));
                        Log.e("E-MAIL ", String.valueOf(dsp.getValue()));
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


    }




    public void onClickFromDatePicker(View view) {
        new DatePickerDialog(this, fromListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener fromListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            from.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
        }
    };

    public void onClickToDatePicker(View view) {
        new DatePickerDialog(this, toListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener toListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            to.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
        }
    };

    public void onFullDayClick(View view) {
        if(frameLayout != null) {
            frameLayout.setVisibility(View.INVISIBLE);
        }
        fullDay = true;

    }

    public void onHoursClick(View view) {
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        HoursFragment hoursFragment = new HoursFragment();
        frameLayout.setVisibility(View.VISIBLE);
        transaction.add(R.id.hours_frame_layout, hoursFragment);
        transaction.commit();
    }

    public void onRequestClick(View view) {
        reasonString = reason.getText().toString();
        fromDate = from.getText().toString();
        toDate = to.getText().toString();
        if (fullDay == true) {
            full = "Yes";
        }
        else {
            full = "No";
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));


//        Log.e("Recepient", recepient);
        intent.putExtra(Intent.EXTRA_EMAIL, recepient);
        intent.putExtra(Intent.EXTRA_SUBJECT, "OD Request");
        intent.putExtra(Intent.EXTRA_TEXT, "Reason: " + reasonString + '\n' + "From: " + fromDate + '\n' + "To: " + toDate + '\n' + "Full day: " + full);
        intent.setType("message/rfc822");
        Intent chooser = Intent.createChooser(intent, "Send email");
        startActivity(chooser);


        Log.e("DATA: ", reasonString + ", " + fromDate + ", " + toDate + ", " + full);
    }

    public void signOut(View view) {
        mAuth.signOut();
        startActivity(new Intent(this, Login.class));
        finish();
    }
}
