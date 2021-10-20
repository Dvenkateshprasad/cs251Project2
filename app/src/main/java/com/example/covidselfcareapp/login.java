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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {
    EditText loginname,password;
    TextView registerdirect;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginname = findViewById(R.id.loginName);
        password = findViewById(R.id.loginPassword);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar2);
        registerdirect = findViewById(R.id.registerDirect);
        registerdirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
                finish();
            }
        });
    }
    public void loginBtnClick(View v){
        String username = loginname.getText().toString().trim();
        String Password = password.getText().toString().trim();
        if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            loginname.setError("Please enter a valid email");
            loginname.requestFocus();
        }
        if(Password.length()<6){
           password.setError("password should be atleast six characters");
           password.requestFocus();
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(username,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
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
                            Intent intent = new Intent(login.this,tabbed.class);
                            intent.putExtra("name",namefromdb);
                            intent.putExtra("gender",genderfromdb);
                            intent.putExtra("age",agefromdb);
                            intent.putExtra("ishealthy",ishealthyfromdb);
                            intent.putExtra("takenquestionnaire",tqfromdb);
                            progressBar.setVisibility(View.GONE);
                            startActivity(intent);
                            finish();
                            Toast.makeText(login.this,"logged in successfully",Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    progressBar.setVisibility(View.GONE);
                }
                else{
                    Toast.makeText(login.this,"user credientials not valid",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}