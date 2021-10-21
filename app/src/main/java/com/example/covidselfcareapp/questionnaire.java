package com.example.covidselfcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class questionnaire extends AppCompatActivity{
    RadioGroup SickGroup;
    RadioGroup con;
    RadioButton Fever;
    RadioButton Breath;
    RadioButton Pain;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    int si,co,fev,bre,pa;
    RadioButton sic,covs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar2);
        SickGroup = (RadioGroup) findViewById(R.id.SickGroup);
        con = (RadioGroup) findViewById(R.id.con);
        Fever = (RadioButton) findViewById(R.id.Fever);
        Breath = (RadioButton) findViewById(R.id.Breath);
        Pain = (RadioButton) findViewById(R.id.Pain);

    }
    public void submitclk(View v) {
        si = SickGroup.getCheckedRadioButtonId();
        sic = (RadioButton) findViewById(si);
        co = con.getCheckedRadioButtonId();
        covs = (RadioButton) findViewById(co);

        if (si == -1 || co == -1) {
            Toast.makeText(questionnaire.this, "dont leave a blank empty", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(questionnaire.this,questionnaire.class);
            startActivity(intent);
        }
        else{
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance()
                    .getCurrentUser().getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                     reference.child("takenquestionnaire").setValue(true);
                    if(!Fever.isChecked() && !Breath.isChecked() && !Pain.isChecked()){
                        reference.child("ishealthy").setValue(true);
                        Toast.makeText(questionnaire.this, "You Are Healthy", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            Intent intent = new Intent(questionnaire.this,tabbed.class);
            startActivity(intent);


        }
    }}



