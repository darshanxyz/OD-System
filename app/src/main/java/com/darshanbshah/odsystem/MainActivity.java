package com.darshanbshah.odsystem;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView welcomeText;
    Calendar calendar = Calendar.getInstance();
    TextView from, to;
    FrameLayout frameLayout;
    EditText reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        welcomeText = (TextView)findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome " + mAuth.getCurrentUser().getEmail());
        from = (TextView)findViewById(R.id.fromDateTV);
        to = (TextView)findViewById(R.id.toDateTV);
        frameLayout = (FrameLayout) findViewById(R.id.hours_frame_layout);
        reason = (EditText)findViewById(R.id.reasonEditText);
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

    }


    StringBuilder checks = new StringBuilder();

    public void hour1Checked(View view) {
        checks.append("1, ");
        Toast.makeText(getApplicationContext(), checks, Toast.LENGTH_SHORT).show();
    }

    public void onHoursClick(View view) {
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        HoursFragment hoursFragment = new HoursFragment();
        frameLayout.setVisibility(View.VISIBLE);
        transaction.add(R.id.hours_frame_layout, hoursFragment);
        transaction.commit();
    }

    String reasonString, fromDate, toDate;
    public void onRequestClick(View view) {
        reasonString = reason.getText().toString();
        fromDate = from.getText().toString();
        toDate = to.getText().toString();

        Log.e("DATA: ", reasonString + ", " + fromDate + ", " + toDate);
    }

    public void signOut(View view) {
        mAuth.signOut();
        startActivity(new Intent(this, Login.class));
        finish();
    }
}
