package com.darshanbshah.odsystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;


public class TeacherActivity extends AppCompatActivity {

    class ODTable {
        String flag, reason, from, to, fullday;

        public ODTable(String flag, String reason, String from, String to, String fullday) {
            this.flag = flag;
            this.reason = reason;
            this.from = from;
            this.to = to;
            this.fullday = fullday;
        }

    }
    String []recepients = {};
    ODTable table;
    int i = 0;
    AlertDialog.Builder builder;
    DataProvider itemValue;
    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    TextView title;

    DatabaseReference root;
    DatabaseReference adv;
    DatabaseReference student;
    DatabaseReference od, od_flag, od_student;
    String message = "";
    List<String> uid_list = new ArrayList<String>();
    HashMap<String, String> uid_map = new HashMap<String, String>();
    HashMap<String, String> student_map = new HashMap<String, String>();
    Hashtable<String, ODTable> odtable = new Hashtable<String, ODTable>();
    ListView listView;
    CustomListAdapter adapter;
    String adv_name, key, value, uid = "";
    List<String> roll = new ArrayList<String>();
    List<String> random = new ArrayList<String>();
    List<String> adv_list = new ArrayList<String>();

    String flag = "", reason = "", from = "", to = "", fullday = "";

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
        title = (TextView)findViewById(R.id.pendingTitle);
        Typeface one = Typeface.createFromAsset(getAssets(), "fonts/AvenirLTStd-Book.otf");
        title.setTypeface(one);
        student.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    uid = dataSnapshot.getKey();
                    if (data.getKey().toString().equals("RollNumber")) {
                        Log.e("MAPVAL", data.getValue().toString());
                        roll.add(data.getValue().toString());
                    }
                }
                for (int a = 0; a < roll.size(); a++) {
                    uid_map.put(uid, roll.get(a));
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

        adv.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (mAuth.getCurrentUser().getEmail().equals(data.getValue().toString())) {
                        adv_name = dataSnapshot.getKey();
                    }
                }
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    adv_list.add(String.valueOf(dsp.getValue()));
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

        od.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals(adv_name)) {
                    Log.e("ADV_NAME", dataSnapshot.getKey());
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Log.e("OD_LIST", data.getKey() + " " + data.getValue());
                        uid_list.add(data.getKey());

                    }
                }
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    for (DataSnapshot d : data.getChildren()) {
                        if (dataSnapshot.getKey().equals(adv_name)) {
                            Log.e("D_VAL", d.getValue().toString());
                            random.add(d.getValue().toString());
                        }

                    }
//                    for (int i = 0; i < random.size(); i++) {
//                        Log.e("RANDOM_VALS", data.getKey() + ' ' + random.get(i));
//
//                    }
                    for (int i = 0; i < random.size(); i++) {
                        if (random.size() != 0) {
                            try {
                                flag = random.get(0);
                                from = random.get(1);
                                fullday = random.get(2);
                                reason = random.get(3);
                                to = random.get(4);
                                table = new ODTable(flag, reason, from, to, fullday);
                                odtable.put(data.getKey(), table);
                            }
                            catch (Exception e) {

                            }

                        }
                    }
                    random.clear();

                }

                adapter = new CustomListAdapter(getApplicationContext(), R.layout.list_item);
                listView.setAdapter(adapter);

                Log.e("UIDLIST_SIZE", String.valueOf(uid_map.size()));

                for (int i = 0; i < uid_list.size(); i++) {
                    key = uid_list.get(i);
                    value = uid_map.get(key);
                    try {
                        if (odtable.get(key).flag.equals("1")) {
                            DataProvider provider = new DataProvider(key, value, odtable.get(key).reason, odtable.get(key).from, odtable.get(key).to);
                            adapter.add(provider);
                        }
                    }
                    catch (Exception e) {

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                Log.e("RCPT", String.valueOf(adv_list.size()));
                builder = new AlertDialog.Builder(TeacherActivity.this);
                builder.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        od.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                DataProvider item_key = (DataProvider)adapter.getItem(position);
                                String item = item_key.getKey();
//                                Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
                                if (dataSnapshot.getKey().equals(adv_name)) {
                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                        if (data.getKey().equals(item)) {
                                            od_flag = od.child(adv_name).child(item).child("flag");
                                            od_flag.setValue(0);
                                            od_student = od.child(adv_name).child(item);
                                            od_student.removeValue();
                                        }
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

                        recepients = adv_list.toArray(new String[0]);
                        Log.e("RCPTSIZE", String.valueOf(recepients.length));
                        for (int i = 0; i < recepients.length; i++) {
                            Log.e("RECPT", recepients[i]);
                        }

                        TextView textView = (TextView)findViewById(R.id.listText);
                        textView.setTextColor(Color.parseColor("#12bfac"));
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setData(Uri.parse("mailto:"));

                        intent.putExtra(Intent.EXTRA_EMAIL, recepients);
                        intent.putExtra(Intent.EXTRA_SUBJECT, "OD Request");
                        intent.putExtra(Intent.EXTRA_TEXT, "Reason: " + reason + '\n' + "From: " + from + '\n' + "To: " + to + '\n' + "Full day: " + fullday);
                        intent.setType("message/rfc822");
                        Intent chooser = Intent.createChooser(intent, "Send email");
                        startActivity(chooser);
                    }
                });
                itemValue = (DataProvider) adapter.getItem(position);
                builder.setMessage("Take Action".toUpperCase());
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


    public void signOut(View view) {
        mAuth.signOut();
        startActivity(new Intent(this, Login.class));
        finish();
    }
}
