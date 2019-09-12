package com.example.mousetrap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    FirebaseDatabase database;
    DatabaseReference myRef;
    Button trapBtn_1;
    Button trapBtn_2;
    public static int superI = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        //add the printted token to firebase to enable push notifications
                        Log.d(TAG, token);
                    }
                });

        System.out.println(FirebaseInstanceId.getInstance().getInstanceId());
        trapBtn_1 = findViewById(R.id.trapBtn_1);
        trapBtn_2 = findViewById(R.id.trapBtn_2);
        basicReadWrite();
        trapBtn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                superI = 1;
                Intent startIntent = new Intent(getApplicationContext(), trap1.class);
                startActivity(startIntent);
            }
        });
        trapBtn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                superI = 1;
                Intent startIntent = new Intent(getApplicationContext(), trap2.class);
                startActivity(startIntent);
            }
        });
    }

    public void basicReadWrite() {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    if (i == 0 && !((String) messageSnapshot.child("Action").getValue()).toLowerCase().equals("none")) {
                        trapBtn_1.setBackgroundColor(Color.parseColor("#DC143C"));
                    } else if (i == 1 && !((String) messageSnapshot.child("Action").getValue()).toLowerCase().equals("none")) {
                        trapBtn_2.setBackgroundColor(Color.parseColor("#DC143C"));
                    } else if (i == 0 && ((String) messageSnapshot.child("Action").getValue()).toLowerCase().equals("none")) {
                        trapBtn_1.setBackgroundColor(Color.parseColor("#DCDCDC"));
                    } else if (i == 1 && ((String) messageSnapshot.child("Action").getValue()).toLowerCase().equals("none")) {
                        trapBtn_2.setBackgroundColor(Color.parseColor("#DCDCDC"));
                    }

                    i++;
                    Log.v("trap1", ((String) (messageSnapshot.child("Action").getValue())).toLowerCase() );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
