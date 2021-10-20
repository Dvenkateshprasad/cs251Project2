package com.example.covidselfcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView loginDirect,errorText;
    EditText edtxtusername,edtxtage,edtxtemail,edtxtpassword2;
    Spinner spinner;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        getSupportActionBar().hide();
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progressBar = findViewById(R.id.progressBar);
        edtxtusername = findViewById(R.id.username);
        edtxtage = findViewById(R.id.age);
        edtxtemail = findViewById(R.id.email);
        edtxtpassword2 = findViewById(R.id.password2);
        spinner = findViewById(R.id.gender);
        loginDirect = findViewById(R.id.loginDirect);
        mAuth=FirebaseAuth.getInstance();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.genders, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
        loginDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),login.class));
                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void registBtnclick(View view){
        String username = edtxtusername.getText().toString().trim();
        String age = edtxtage.getText().toString().trim();
        String gender = spinner.getSelectedItem().toString().trim();
        String password = edtxtpassword2.getText().toString().trim();
        String email = edtxtemail.getText().toString().trim();

        if(username.isEmpty()){
            edtxtusername.setError("please enter a username");
            edtxtusername.requestFocus();
//            return;
        }
        if(age.isEmpty()){
            edtxtage.setError("please enter your age");
            edtxtage.requestFocus();
//            return;
        }
        if(gender.equals("Select Gender")){
            errorText = (TextView)spinner.getSelectedView();
            errorText.setError("anything here, just to add the icon");
            spinner.requestFocus();
//            return;
        }
        if(password.isEmpty()||password.length()<6){
            edtxtpassword2.setError("password should contain atleast six characters");
            edtxtpassword2.requestFocus();
//            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtxtemail.setError("please enter a valid email address");
            edtxtemail.requestFocus();
//            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(username,age,gender,email,password);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Register.this,"user registered successfully",Toast.LENGTH_LONG).show();
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String namefromdb = snapshot.child(email).child("username").getValue(String.class);
                                                String genderfromdb = snapshot.child(email).child("gender").getValue(String.class);
                                                String agefromdb = snapshot.child(email).child("age").getValue(String.class);
                                                boolean ishealthyfromdb = snapshot.child(email).child("ishealthy").getValue(Boolean.class);
                                                boolean tqfromdb = snapshot.child(email).child("takenquestionnaire").getValue(Boolean.class);
                                                Intent intent = new Intent(getApplicationContext(),tabbed.class);
                                                intent.putExtra("name",namefromdb);
                                                intent.putExtra("gender",genderfromdb);
                                                intent.putExtra("age",agefromdb);
                                                intent.putExtra("ishealthy",ishealthyfromdb);
                                                intent.putExtra("takenquestionnaire",tqfromdb);
                                                progressBar.setVisibility(View.GONE);
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                    }
                                    else{
                                        Toast.makeText(Register.this,"user registration failed",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);

                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(Register.this,"user registration failed",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }
}