package com.example.mousetrap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class trap1 extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    public String OpenTrap_string;
    public String CloseTrap_string;
    public String EstadoTrap_string;
    public String PhotoTrap_string;
    public String URL = "";
    Button btnView_trap1;
    Button btnBack;
    Button btnOpen;
    Button btnClose;
    TextView textStatus;
    ValueEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trap1);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        database.goOnline();
        btnView_trap1 = findViewById(R.id.btnView_trap1);
        btnBack = findViewById(R.id.btnBackTrap1);
        btnOpen = findViewById(R.id.openBtn_trap1);
        btnClose = findViewById(R.id.closeBtn_trap1);
        textStatus = findViewById(R.id.statusLabelTrap1);
        basicReadWrite();

        btnView_trap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOpenImg();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToMain();
            }
        });

        btnOpen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                clickOpenTrap();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCloseTrap();
            }
        });
    }

    private void clickOpenImg() {
        DatabaseReference myRef = database.getReference("trap1/Photo");
        myRef.setValue("Waiting");
    }

    private void clickCloseTrap() {
        DatabaseReference myRef = database.getReference("trap1/CloseTrap");
        myRef.setValue("Close");

    }

    private void clickOpenTrap() {
        DatabaseReference myRef = database.getReference("trap1/OpenTrap");
        myRef.setValue("Open");
    }

    private void goBackToMain() {
        myRef.removeEventListener(listener);
        finish();
    }

    private void loadImage(){
        //show pic
        ImageView imgView_trap1 = findViewById(R.id.imgView_trap1);
        Log.v("TRAP1","URl " + URL);
        Picasso.with(this).load(URL).into(imgView_trap1);
        URL = "";

    }


    public void basicReadWrite() {
        listener = myRef.addValueEventListener(new ValueEventListener() {
            int i = 0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    if (i == 0) {
                        EstadoTrap_string = (String) messageSnapshot.child("Estado").getValue();
                        textStatus.setText(EstadoTrap_string);
                        textStatus.setBackgroundColor(Color.parseColor("#DCDCDC"));
                        if ( !((String) messageSnapshot.child("Url").getValue()).toLowerCase().equals("none")) {
                            URL = (String) messageSnapshot.child("Url").getValue();
                            DatabaseReference myRef = database.getReference("trap1/Url");
                            myRef.setValue("None");
                            loadImage();
                        }
                        break;
                    }
                    i++;
                }
                Log.v("trap1", "basicread" + OpenTrap_string + " close " + CloseTrap_string + " es " + EstadoTrap_string +" asd " +PhotoTrap_string);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
