package com.darshanbshah.odsystem;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
    DatabaseReference od, od_flag, od_from, od_to, od_reason, od_full_day;

    RollNumber r = new RollNumber();

    String recepient = "";

    List<String> list1 = new ArrayList<String>();
    List<String> list_adv_name = new ArrayList<String>();
    List<String> list_adv_email = new ArrayList<String>();
    HashMap<String, String> adv_details = new HashMap<String, String>();


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
        od = root.child("OD");

        welcomeText = (TextView)findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome " + mAuth.getCurrentUser().getEmail());
        from = (TextView)findViewById(R.id.fromDateTV);
        to = (TextView)findViewById(R.id.toDateTV);
        frameLayout = (FrameLayout) findViewById(R.id.hours_frame_layout);
        reason = (EditText)findViewById(R.id.reasonEditText);

        student.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(final DataSnapshot d : dataSnapshot.getChildren()){
                    student.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot ds) {
                            for(DataSnapshot dsp : ds.getChildren()){
                                if (dsp.getKey().toString().equals(mAuth.getCurrentUser().getUid()))  {
                                    if (d.getKey().toString().equals("Advisor")) {
                                        list1.add(String.valueOf(d.getValue()));
                                        Log.e("E-MAIL of Adv ", String.valueOf(d.getValue()));
                                    }

                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

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
        for (int i = 0; i < list_adv_name.size(); i++) {
            list_adv_name.remove(i);
        }
        adv.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    list_adv_name.add(data.getKey().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        for (int i = 0; i < list_adv_email.size(); i++) {
            list_adv_email.remove(i);
        }

        adv.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.getKey().toString().equals("Email")) {
                        list_adv_email.add(data.getValue().toString());
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
        fullDay = false;
    }

    public void onRequestClick(View view) {
        for (int i = 0; i < list_adv_name.size(); i++) {
            adv_details.put(list_adv_email.get(i), list_adv_name.get(i));
        }

        reasonString = reason.getText().toString();
        fromDate = from.getText().toString();
        toDate = to.getText().toString();
        if (fullDay == true) {
            full = "Yes";
        }
        else {
            full = "No";
        }
        final String str = adv_details.get(list1.get(0));

        od_flag = od.child(str).child(mAuth.getCurrentUser().getUid()).child("flag");
        od_from = od.child(str).child(mAuth.getCurrentUser().getUid()).child("from");
        od_to = od.child(str).child(mAuth.getCurrentUser().getUid()).child("to");
        od_reason = od.child(str).child(mAuth.getCurrentUser().getUid()).child("reason");
        od_full_day = od.child(str).child(mAuth.getCurrentUser().getUid()).child("full day");

        od_flag.setValue(1);
        od_from.setValue(fromDate);
        od_to.setValue(toDate);
        od_reason.setValue(reasonString);
        od_full_day.setValue(full);

        reason.setText("");
        from.setText("");
        to.setText("");

//        recepient = list1.get(0);
//



        Log.e("DATA: ", reasonString + ", " + fromDate + ", " + toDate + ", " + full);
    }

    public void signOut(View view) {
        mAuth.signOut();
        startActivity(new Intent(this, Login.class));
        finish();
    }
}
