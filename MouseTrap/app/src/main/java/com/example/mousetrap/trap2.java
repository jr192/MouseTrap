package com.example.mousetrap;

import android.graphics.Color;
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

public class trap2 extends AppCompatActivity {
    String TRAP = "TRAP2";

    public String OpenTrap_string;
    public String CloseTrap_string;
    public String EstadoTrap_string;
    public String PhotoTrap_string;
    public String URL = "";
    Button btnView_trap2;
    Button btnBack;
    Button btnOpen;
    Button btnClose;
    TextView textStatus;
    ValueEventListener listener;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trap2);
        btnView_trap2 = findViewById(R.id.btnView_trap2);
        btnBack = findViewById(R.id.btnBacktrap2);
        btnOpen = findViewById(R.id.openBtn_trap2);
        btnClose = findViewById(R.id.closeBtn_trap2);
        textStatus = findViewById(R.id.statusLabelTrap2);
        basicReadWrite();

        btnView_trap2.setOnClickListener(new View.OnClickListener() {
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
        DatabaseReference myRef = database.getReference("trap2/Photo");
        myRef.setValue("Waiting");


    }
    private void clickCloseTrap() {
        DatabaseReference myRef = database.getReference("trap2/CloseTrap");
        myRef.setValue("Close");
    }

    private void clickOpenTrap() {
        myRef = database.getReference("trap2/OpenTrap");
        myRef.setValue("Open");
    }

    private void goBackToMain() {
        myRef.removeEventListener(listener);
        finish();
    }

    private void loadImage(){
        ImageView imgView_trap2 = findViewById(R.id.imgView_trap2);
        Log.v("TRAP2","URl " + URL);
        Picasso.with(this).load(URL).into(imgView_trap2);
        URL = "";
    }
    public void basicReadWrite() {

       listener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    if (i == 1) {
                        EstadoTrap_string = (String) messageSnapshot.child("Estado").getValue();
                        textStatus.setText(EstadoTrap_string);
                        textStatus.setBackgroundColor(Color.parseColor("#DCDCDC"));
                        if (!((String) messageSnapshot.child("Url").getValue()).toLowerCase().equals("none")) {
                            URL = (String) messageSnapshot.child("Url").getValue();
                            DatabaseReference myRef = database.getReference("trap2/Url");
                            myRef.setValue("None");
                            loadImage();
                        }
                        break;
                    }
                    i++;
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}

