package com.example.covidselfcareapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.covidselfcareapp.ui.main.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class tabbed extends AppCompatActivity {
    TextView name,age,gender,healthstatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String namefromdb = snapshot.child("username").getValue(String.class);
                String genderfromdb = snapshot.child("gender").getValue(String.class);
                String agefromdb = snapshot.child("age").getValue(String.class);
                boolean ishealthyfromdb = snapshot.child("ishealthy").getValue(Boolean.class);
                boolean tqfromdb = snapshot.child("takenquestionnaire").getValue(Boolean.class);
                name = findViewById(R.id.textView2);
                age = findViewById(R.id.textView3);
                gender = findViewById(R.id.textView4);
                healthstatus = findViewById(R.id.textView5);
                name.setText("Name : "+namefromdb);
                age.setText("Age : "+agefromdb);
                gender.setText("Gender :  "+genderfromdb);
                if(!tqfromdb){
                    healthstatus.setText("Health Status: "+"Please take questionnaire");
                }
                else{
                    if(ishealthyfromdb){
                        healthstatus.setText("Health Status: "+"Healthy");
                    }
                    else{
                        healthstatus.setText("Health Status: "+"AT RISK");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(tabbed.this,questionnaire.class);
                startActivity(intent);
            }
        });
    }
}