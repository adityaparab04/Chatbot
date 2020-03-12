package com.viva.VChat.ChatBot;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vmac.ChatBot.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {
    private EditText newLoginEmail, newLoginPassword;
    private Button nBtnRegister,nBtnSignIn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setUpUIViews();//will set up ui id references

        firebaseAuth=FirebaseAuth.getInstance();

        //Registration Button Logic
        nBtnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(validate())
                {
                    //upload data to firebase
                String user_email = newLoginEmail.getText().toString().trim(); //trim is used to remove all the white spaces that user may have entered
                String user_password = newLoginPassword.getText().toString().trim();
                firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                  @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                  {
                        if(task.isSuccessful())
                         {
                            //Toast.makeText(SignupActivity.this,"Registration Successful", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                             sendEmailVerification();
                         }
                         else
                         {
                            Toast.makeText(SignupActivity.this,"Registration Failed", Toast.LENGTH_SHORT).show();

                         }
                  }
                });
                }

            }
        });

        //LogIn Button Logic
        nBtnSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"LogIn Activity ",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));

            }
        });
    }
    //setting up ui id references
    private void setUpUIViews()
    {

        newLoginEmail =findViewById(R.id.nLoginEmail);
        newLoginPassword =findViewById(R.id.nLoginPassword);


        nBtnRegister=findViewById(R.id.nBtnRegister);
        nBtnSignIn=findViewById(R.id.nBtnSignIn);

    }
    private boolean validate()
    {
        Boolean result=false;


        String email=newLoginEmail.getText().toString();
        String password=newLoginPassword.getText().toString();
        if(email.isEmpty()|| password.isEmpty())
        {
           Toast.makeText(this,"Please enter details for registration",Toast.LENGTH_SHORT).show();
        }

        else
        {
            result=true;
        }
        return result;
    }
    private void sendEmailVerification()
    {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(SignupActivity.this,"successfully registered, Verification email has been sent !",Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                        startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                    }else
                        Toast.makeText(SignupActivity.this,"Verification email has not been sent !",Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}
