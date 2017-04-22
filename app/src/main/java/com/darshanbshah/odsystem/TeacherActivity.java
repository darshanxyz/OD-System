package com.darshanbshah.odsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
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
import java.util.HashMap;
import java.util.List;

public class TeacherActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference root;
    DatabaseReference od;
    DatabaseReference adv;
    DatabaseReference student;

    List<String> uid_list = new ArrayList<String>();
    HashMap<String, String> uid_map = new HashMap<String, String>();
    HashMap<String, String> student_map = new HashMap<String, String>();

    ListView listView;

    CustomListAdapter adapter;

    String adv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        mAuth = FirebaseAuth.getInstance();
        root = database.getReference();
        od = root.child("OD");
        adv = root.child("Advisors");
        student = root.child("Student");
        listView = (ListView)findViewById(R.id.listView);

        adv.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (mAuth.getCurrentUser().getEmail().equals(data.getValue().toString())) {
                        Log.e("DATA_KEY", dataSnapshot.getKey());
                        adv_name = dataSnapshot.getKey();

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

        od.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals(adv_name)) {
                    Log.e("SNAP", dataSnapshot.getKey());
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Log.e("OD_LIST", data.getKey() + " " + data.getValue());
                        uid_map.put(data.getKey(), data.getValue().toString());
                        uid_list.add(data.getKey());
                    }
                }
                adapter = new CustomListAdapter(getApplicationContext(), R.layout.list_item);
                listView.setAdapter(adapter);

                Log.e("UIDLIST_SIZE", String.valueOf(uid_list.size()));
                for (String value: uid_list) {
                    DataProvider provider = new DataProvider(value);
                    adapter.add(provider);
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
//                for(DataSnapshot dsp : dataSnapshot.getChildren()){
//                    if (uid_list.contains(dataSnapshot.getKey())) {
//                        if (dsp.getKey().toString().equals("RollNumber"))  {
//
//                        }
//                    }
//                }

                //Code yet to be decided

//                for (int i = 0; i < uid_list.size(); i++) {
//                    for (DataSnapshot data : dataSnapshot.getChildren()) {
//                        if (dataSnapshot.getKey().equals(uid_list.get(i))) {
//                            if (data.getKey().equals("RollNumber")) {
//                                student_map.put(uid_list.get(i), data.getValue().toString());
//                            }
//                        }
//                    }
//
//                }
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

    public void signOut(View view) {
        mAuth.signOut();
        startActivity(new Intent(this, Login.class));
        finish();
    }
}
