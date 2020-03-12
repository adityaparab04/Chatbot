package com.viva.VChat.ChatBot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.IDNA;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.vmac.ChatBot.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity  {
    // UI references.
    Button btnSignUp, btnLogin;
    EditText loginEmail,loginPassword;

    private  ProgressDialog progressDialog;

    //Declaring an instance of FirebaseAuth
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.


    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        loginEmail =  findViewById(R.id.loginEmail);
        loginPassword =  findViewById(R.id.loginPassword);
        btnSignUp =findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogin);

        //Initialising FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        //progressDialog
        progressDialog=new ProgressDialog(this);

        if(currentUser !=null)
        {
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }

        //SIGNUP BUTTON
        btnSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Sign Up Activity ",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });

        //LOGIN BUTTON
            btnLogin.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (loginEmail.getText().toString().isEmpty() || loginPassword.getText().toString().isEmpty())
                    {
                        Toast.makeText(LoginActivity.this,"Enter Email and Password",Toast.LENGTH_LONG).show();
                    }
                    else
                    validate(loginEmail.getText().toString(),loginPassword.getText().toString());
                }
            });
    }

    private void validate(String username,String password) {

            progressDialog.setMessage("Verifying details with the Firebase");
            progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {

                  //  Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_LONG).show();
                  checkEmailVerification();
                }
                else
                    {
                    Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_LONG).show();
                   }
                   progressDialog.dismiss();
            }
        });
    }
    private void checkEmailVerification()
    {
        FirebaseUser firebaseUser=firebaseAuth.getInstance().getCurrentUser();
        Boolean emailFlag=firebaseUser.isEmailVerified();
        if(emailFlag)
        {
            finish();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }else
            Toast.makeText(LoginActivity.this,"Verify your Email",Toast.LENGTH_LONG).show();
        firebaseAuth.signOut();
    }
}

